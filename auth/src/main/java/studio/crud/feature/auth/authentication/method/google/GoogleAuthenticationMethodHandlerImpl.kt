package studio.crud.feature.auth.authentication.method.google

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.modelfilter.dsl.where
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson.JacksonFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import studio.crud.feature.auth.authentication.method.base.AuthenticationMethodHandler
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.auth.authentication.method.google.model.GoogleAuthenticationPayloadDTO
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.config.properties.authentication.GoogleAuthenticationProperties
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.auth.exception.GoogleIncorrectClientIdException
import studio.crud.feature.auth.exception.GoogleTokenVerificationFailedException
import studio.crud.feature.auth.exception.GoogleUidMismatchException
import kotlin.reflect.KClass

@Component
@ConditionalOnProperty(prefix = GoogleAuthenticationProperties.PREFIX, name = ["enabled"], havingValue = "true")
class GoogleAuthenticationMethodHandlerImpl(
    private val properties: GoogleAuthenticationProperties,
    private val crudHandler: CrudHandler
) : AuthenticationMethodHandler<GoogleAuthenticationPayloadDTO> {

    override val methodType: AuthenticationMethodType
        get() = AuthenticationMethodType.Google

    override val payloadType: KClass<GoogleAuthenticationPayloadDTO> = GoogleAuthenticationPayloadDTO::class

    override fun getUsernameFromPayload(payload: GoogleAuthenticationPayloadDTO): String {
        return payload.idToken!!
    }

    override fun doLogin(payload: GoogleAuthenticationPayloadDTO, method: EntityAuthenticationMethod) {
        val idToken = validateIdToken(payload.idToken!!)
        if(idToken.payload.subject != method.uid) {
            throw GoogleUidMismatchException()
        }
    }

    override fun doRegister(payload: GoogleAuthenticationPayloadDTO, params: CustomParamsDTO, entity: Entity): EntityAuthenticationMethod {
        val idToken = validateIdToken(payload.idToken!!)
        return EntityAuthenticationMethod(
                entity,
                AuthenticationMethodType.Google,
                idToken.payload.subject,
                idToken.payload.email
        )
    }

    override fun getEntityMethod(payload: GoogleAuthenticationPayloadDTO): EntityAuthenticationMethod? {
        val idToken = validateIdToken(payload.idToken!!)
        return crudHandler.showBy(where {
            "param1" Equal idToken.payload.subject
            "methodType" Equal  AuthenticationMethodType.Google
        }, EntityAuthenticationMethod::class.java)
                .execute()
    }

    override fun getUsername(method: EntityAuthenticationMethod): String {
        return method.email
    }

    private fun validateIdToken(idToken: String): GoogleIdToken {
        val token: GoogleIdToken = try {
            tokenVerifier.verify(idToken)
        } catch (e: Exception) {
            throw GoogleTokenVerificationFailedException().initCause(e)
        }

        if (!token.verifyAudience(listOf(properties.clientId))) {
            throw GoogleIncorrectClientIdException()
        }
        return token
    }

    companion object {
        private val transport: HttpTransport = NetHttpTransport()
        private val jsonFactory: JsonFactory = JacksonFactory()
        private val tokenVerifier = GoogleIdTokenVerifier(transport, jsonFactory)

        /**
         * Extensions
         */
        private var EntityAuthenticationMethod.uid
            get() = this.param1!!
            set(value) {
                this.param1 = value
            }

        private var EntityAuthenticationMethod.email
            get() = this.param2!!
            set(value) {
                this.param2 = value
            }
    }
}