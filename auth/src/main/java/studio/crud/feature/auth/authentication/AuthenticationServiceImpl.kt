package studio.crud.feature.auth.authentication

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.modelfilter.dsl.where
import com.antelopesystem.crudframework.utils.component.componentmap.annotation.ComponentMap
import mu.KotlinLogging
import org.springframework.stereotype.Component
import studio.crud.feature.auth.authentication.method.base.AuthenticationMethodHandler
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.auth.authentication.method.enums.UsernameType
import studio.crud.feature.auth.authentication.model.AuthenticationResultDTO
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.authentication.model.ForgotPasswordToken
import studio.crud.feature.auth.authentication.notifier.AuthenticationPostProcessor
import studio.crud.feature.auth.authentication.validation.*
import studio.crud.feature.auth.config.properties.AuthProperties
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.exception.*
import studio.crud.feature.auth.model.TokenPayload
import studio.crud.feature.auth.model.UserInfo
import studio.crud.feature.auth.token.TokenHandler
import studio.crud.sharedcommon.exception.core.ServerException
import studio.crud.sharedcommon.utils.extractAndValidatePayload
import java.util.*

@Component
class AuthenticationServiceImpl(
        private val tokenHandler: TokenHandler,
        private val authenticationPostProcessor: AuthenticationPostProcessor,
        private val crudHandler: CrudHandler,
        private val authProperties: AuthProperties
) : AuthenticationService {

    private val setups = mutableMapOf<UserPair, CustomParamsDTO>()

    @ComponentMap
    private lateinit var authenticationMethodTypeHandlers: Map<AuthenticationMethodType, AuthenticationMethodHandler<Any>>

    override fun initializeRegistration(methodType: AuthenticationMethodType, body: String): CustomParamsDTO {
        log.trace { "Received initializeRegistration with payload: {$body}" }

        val methodHandler = getMethodHandler(methodType)
        val payload = extractAndValidatePayload(body, methodHandler.payloadType, InitializeRegistrationValidation::class.java)
        methodHandler.getEntityMethod(payload)?.let {
            if(methodHandler.methodType.canCrossActions) {
                log.trace { "[ Method ID: ${it.id}, Method Type: ${it.methodType}, Entity: ${it.entity.id} ] exists and allowLoginOnRegistration=true, switching to initializeLogin" }
                return initializeLogin(methodType, body)
            }
            log.trace { "[ Method ID: ${it.id}, Method Type: ${it.methodType}, Entity: ${it.entity.id} ] exists and canCrossActions=false, terminating" }
            throw EntityAlreadyExistsException()
        }

        val username = methodHandler.getUsernameFromPayload(payload)
        val params = methodHandler.initializeRegistration(payload)
        setups[UserPair(username, methodHandler.methodType)] = params
        log.trace { "Initialized registration for username: $username, params: $params, method: ${methodHandler.methodType}" }
        return params
    }

    override fun doRegister(methodType: AuthenticationMethodType, body: String): AuthenticationResultDTO {
        log.trace { "Received doRegister with payload: {$body}" }
        val methodHandler = getMethodHandler(methodType)
        val payload = extractAndValidatePayload(body, methodHandler.payloadType, RegistrationValidation::class.java)
        try {
            val username = methodHandler.getUsernameFromPayload(payload)
            methodHandler.getEntityMethod(payload)?.let {
                if(methodHandler.methodType.canCrossActions) {
                    log.trace { "[ Method ID: ${it.id}, Method Type: ${it.methodType}, Entity: ${it.entity.id} ] exists and canCrossActions=true, switching to doLogin" }
                    return doLogin(methodType, body)
                }
                log.trace { "[ Method ID: ${it.id}, Method Type: ${it.methodType}, Entity: ${it.entity.id} ] exists and canCrossActions=false, terminating" }
                throw EntityNotFoundException()
            }

            var params = setups[UserPair(username, methodHandler.methodType)]
            if(params == null && methodHandler.methodType.registrationInitializationRequired) {
                throw RegistrationNotInitializedException()
            } else {
                params = CustomParamsDTO()
            }

            val entity = Entity()
            val method = methodHandler.doRegister(payload, params, entity)
            method.primary = true
            entity.authenticationMethods.add(method)
            when(method.methodType.usernameType) {
                UsernameType.Email -> {
                    val email = methodHandler.getUsername(method)
                    val existingEntity = crudHandler.showBy(where {
                        "email" Equal email
                    }, Entity::class.java)
                            .fromCache()
                            .execute()
                    if(existingEntity != null) {
                        throw EmailInUseException()
                    }

                    entity.email = email
                }
                UsernameType.Telephone -> {
                    val telephone = methodHandler.getUsername(method)
                    val existingEntity = crudHandler.showBy(where {
                        "telephone" Equal telephone
                    }, Entity::class.java)
                            .fromCache()
                            .execute()
                    if(existingEntity != null) {
                        throw TelephoneInUseException()
                    }
                    entity.telephone = methodHandler.getUsername(method)
                }
                else -> {}
            }
            crudHandler.create(entity).execute()
            log.trace { "Performed registration for [ Method ID: ${method.id}, Method Type: ${method.methodType}, Entity: ${method.entity.id} ], params: [ $params ]" }
            val parsedToken = tokenHandler.generateToken(
                TokenPayload(
                    entity.uuid,
                    false,
                    false,
                    Date(System.currentTimeMillis() + authProperties.tokenLifetimeHours * 60L * 60L * 1000L)
                )
            )
            authenticationPostProcessor.onRegistrationSuccess(body, method, parsedToken)
            return AuthenticationResultDTO(entity.uuid, parsedToken.token)
        } catch(e: ServerException) {
            authenticationPostProcessor.onRegistrationFailure(body, e.message.toString())
            throw e
        } catch(e: Exception) {
            authenticationPostProcessor.onRegistrationFailure(body, e.message ?: UNHANDLED_EXCEPTION)
            log.error(e) { "Registration failed wjth unknown exception" }
            throw e
        }
    }

    override fun initializeLogin(methodType: AuthenticationMethodType, body: String): CustomParamsDTO {
        log.trace { "Received initializeLogin with payload: {$body}" }
        val methodHandler = getMethodHandler(methodType)
        val payload = extractAndValidatePayload(body, methodHandler.payloadType, InitializeLoginValidation::class.java)
        val entityMethod = methodHandler.getEntityMethod(payload)
        if(entityMethod == null) {
            if(methodHandler.methodType.canCrossActions) {
                log.trace { "Username [ ${methodHandler.getUsernameFromPayload(payload)} ] for method [ ${methodHandler.methodType} ] does not exist and canCrossActions=true, switching to initializeRegistration" }
                return initializeRegistration(methodType, body)
            }
            log.trace { "Username [ ${methodHandler.getUsernameFromPayload(payload)} ] for method [ ${methodHandler.methodType} ] does not exist and canCrossActions=false, terminating" }
            throw EntityNotFoundException()
        }
        return methodHandler.initializeLogin(body, entityMethod)
    }

    override fun doLogin(methodType: AuthenticationMethodType, body: String): AuthenticationResultDTO {
        log.trace { "Received initializeLogin with payload: {$body}" }
        val methodHandler = getMethodHandler(methodType)
        val payload = extractAndValidatePayload(body, methodHandler.payloadType, LoginValidation::class.java)
        val entityMethod = methodHandler.getEntityMethod(payload)
        if(entityMethod == null) {
            if(methodHandler.methodType.canCrossActions) {
                log.trace { "Username [ ${methodHandler.getUsernameFromPayload(payload)} ] for method [ ${methodHandler.methodType} ] does not exist and allowRegistrationOnLogin=true, switching to initializeRegistration" }
                return doRegister(methodType, body)
            }
            log.trace { "Username [ ${methodHandler.getUsernameFromPayload(payload)} ] for method [ ${methodHandler.methodType} ] does not exist and allowRegistrationOnLogin=false or canCrossActions=false, terminating" }
            throw EntityNotFoundException()
        }

        try {
            log.trace { "Performing login request for [ Method ID: ${entityMethod.id}, Method Type: ${entityMethod.methodType}, Entity: ${entityMethod.entity.id} ], method: [ ${methodHandler.methodType} ]" }
            methodHandler.doLogin(payload, entityMethod)
            log.trace { "Performed login request for [ Method ID: ${entityMethod.id}, Method Type: ${entityMethod.methodType}, Entity: ${entityMethod.entity.id} ], method: [ ${methodHandler.methodType} ]" }
            val parsedToken = tokenHandler.generateToken(
                TokenPayload(
                    entityMethod.entity.uuid,
                    false,
                    false,
                    Date(System.currentTimeMillis() + authProperties.tokenLifetimeHours * 60L * 60L * 1000L)
                )
            )
            authenticationPostProcessor.onLoginSuccess(body, entityMethod, parsedToken)
            return AuthenticationResultDTO(entityMethod.entity.uuid, parsedToken.token)
        } catch(e: ServerException) {
            authenticationPostProcessor.onLoginFailure(body, entityMethod, e.message.toString())
            throw e
        } catch(e: Exception) {
            authenticationPostProcessor.onLoginFailure(body, entityMethod, UNHANDLED_EXCEPTION)
            log.error(e) { "Login failed with unknown exception" }
            throw e
        }
    }

    override fun initializeForgotPassword(methodType: AuthenticationMethodType, body: String) {
        log.trace { "Received initializeForgotPassword with payload: {$body}" }
        if(!methodType.passwordBased) {
            log.error { "initializeForgotPassword failed for method [ $methodType ] as it is not password based" }
            throw ForgotPasswordNotSupportedException(methodType)
        }

        val methodHandler = getMethodHandler(methodType)
        val payload = extractAndValidatePayload(body, methodHandler.payloadType, InitializeForgotPasswordValidation::class.java)

        val method = methodHandler.getEntityMethod(payload) ?: throw EntityNotFoundException()
        val token = crudHandler.create(ForgotPasswordToken(method, method.entity.id)).execute()
        authenticationPostProcessor.onForgotPasswordInitialized(token.token, method)
        log.trace { "Performed initializeForgotPassword for [ Method ID: ${method.id}, Method Type: ${method.methodType}, Entity: ${method.entity.id} ]: {$body}" }
    }

    override fun redeemForgotPasswordToken(tokenString: String, newPassword: String) {
        log.trace { "Received redeemForgotPasswordToken with token: [ {$tokenString} ]" }
        val token = crudHandler.showBy(where {
            "token" Equal tokenString
        }, ForgotPasswordToken::class.java)
                .execute()
                ?: throw InvalidForgotPasswordTokenException()

        val method = token.method
        val methodHandler = getMethodHandler(method.methodType)

        if(!methodHandler.methodType.passwordBased) {
            log.error { "redeemForgotPasswordToken failed for method [ ${methodHandler.methodType} ] as it is not password based" }
            throw ForgotPasswordNotSupportedException(methodHandler.methodType)
        }

        methodHandler.changePassword(newPassword, method)
        crudHandler.update(method).execute()
        authenticationPostProcessor.onForgotPasswordSuccess(token, method)
        crudHandler.delete(token.id, ForgotPasswordToken::class.java).execute()
        log.trace { "Performed redeemForgotPasswordToken for [ Method ID: ${method.id}, Method Type: ${method.methodType}, Entity: ${method.entity.id} ]" }
    }


    override fun changePassword(methodType: AuthenticationMethodType, body: String, newPassword: String, userInfo: UserInfo): String {
        val methodHandler = getMethodHandler(methodType)
        val payload = extractAndValidatePayload(body, methodHandler.payloadType, ChangePasswordValidation::class.java)
        val entityMethod = methodHandler.getEntityMethod(payload) ?: throw EntityNotFoundException()
        if(!methodHandler.checkPassword(payload, entityMethod)) {
            throw OldPasswordMismatchException()
        }

        methodHandler.changePassword(newPassword, entityMethod)
        val parsedToken = userInfo.parsedToken
        if(parsedToken.payload.passwordChangeRequired) {
            val newPayload = parsedToken.payload.copy(passwordChangeRequired = false)
            return tokenHandler.generateToken(newPayload).token
        }

        return userInfo.parsedToken.token
    }

    private fun getMethodHandler(methodType: AuthenticationMethodType): AuthenticationMethodHandler<Any> {
        val methodHandler = authenticationMethodTypeHandlers[methodType] ?: throw AuthenticationMethodNotSupportedException(methodType)
        return methodHandler
    }

    override fun getAvailableMethods(): List<AuthenticationMethodDTO> {
        return authenticationMethodTypeHandlers.map { AuthenticationMethodDTO(it.key, it.key == authProperties.defaultAuthenticationMethodType) }
        }

    companion object {
        private val UNHANDLED_EXCEPTION = "Unhandled exception"
        private val log = KotlinLogging.logger { }

        private data class UserPair(val username: String, val methodType: AuthenticationMethodType)

        data class AuthenticationMethodDTO(val methodType: AuthenticationMethodType, val default: Boolean)
    }
}

