package studio.crud.feature.auth.authentication.method.emailpassword

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.modelfilter.dsl.where
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import studio.crud.feature.auth.authentication.method.base.AuthenticationMethodHandler
import studio.crud.feature.auth.authentication.method.emailpassword.model.EmailPasswordAuthenticationPayloadDTO
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.config.properties.authentication.EmailPasswordAuthenticationProperties
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.auth.exception.InvalidEmailOrPasswordException
import java.util.*
import kotlin.reflect.KClass

@Component
@ConditionalOnProperty(prefix = EmailPasswordAuthenticationProperties.PREFIX, name = ["enabled"], havingValue = "true")
class EmailPasswordAuthenticationMethodHandlerImpl(
    private val crudHandler: CrudHandler,
    private val properties: EmailPasswordAuthenticationProperties,
    private val passwordEncoder: PasswordEncoder
) : AuthenticationMethodHandler<EmailPasswordAuthenticationPayloadDTO> {
    override val methodType: AuthenticationMethodType
        get() = AuthenticationMethodType.EmailPassword

    override val payloadType: KClass<EmailPasswordAuthenticationPayloadDTO> = EmailPasswordAuthenticationPayloadDTO::class

    override fun getEntityMethod(payload: EmailPasswordAuthenticationPayloadDTO): EntityAuthenticationMethod? {
        return crudHandler.showBy(where {
            "param1" Equal payload.email!!
            "methodType" Equal  AuthenticationMethodType.EmailPassword
        }, EntityAuthenticationMethod::class.java)
            .execute()
    }

    // todo: add email validation
    override fun doLogin(payload: EmailPasswordAuthenticationPayloadDTO, method: EntityAuthenticationMethod) {
        val emailMatches = payload.email == method.email
        if (!emailMatches) {
            throw InvalidEmailOrPasswordException()
        }
        val passwordMatches = checkPassword(payload, method)
        if (!passwordMatches) {
            throw InvalidEmailOrPasswordException()
        }
    }

    override fun doRegister(payload: EmailPasswordAuthenticationPayloadDTO, params: CustomParamsDTO, entity: Entity): EntityAuthenticationMethod {
        val method = EntityAuthenticationMethod(entity, AuthenticationMethodType.EmailPassword)
        method.email = payload.email!!
        changePassword(payload.password!!, method)
        return method
    }

    override fun getUsernameFromPayload(payload: EmailPasswordAuthenticationPayloadDTO): String {
        return payload.email!!
    }

    override fun changePassword(newPassword: String, method: EntityAuthenticationMethod) {
        method.password = passwordEncoder.encode(newPassword)
        refreshPasswordExpiryTime(method)
    }

    override fun checkPassword(payload: EmailPasswordAuthenticationPayloadDTO, method: EntityAuthenticationMethod): Boolean {
        return passwordEncoder.matches(payload.password, method.password)
    }

    override fun isPasswordExpired(method: EntityAuthenticationMethod): Boolean {
        if(method.passwordExpiryTime == null) return false
        return method.passwordExpiryTime!!.before(Date())
    }

    override fun getUsername(method: EntityAuthenticationMethod): String {
        return method.email
    }

    private fun refreshPasswordExpiryTime(method: EntityAuthenticationMethod) {
        method.passwordExpiryTime = Date(System.currentTimeMillis() + properties.passwordExpiryDays * 24L * 60L * 60L * 1000L)
    }

    companion object {
        private var EntityAuthenticationMethod.email
            get() = this.param1!!
            set(value) {
                this.param1 = value
            }

        private var EntityAuthenticationMethod.password
            get() = this.param2!!
            set(value) {
                this.param2 = value
            }

        private var EntityAuthenticationMethod.passwordExpiryTime: Date?
            get() = if(this.param3.isNullOrBlank()) {
                null
            } else {
                Date(this.param3!!.toLong())
            }
            set(value: Date?) {
                this.param3 = value?.time.toString()
            }

        private val CustomParamsDTO.email get() = this.param1

        private val CustomParamsDTO.password get() = this.param2
    }
}