package studio.crud.feature.auth.authentication.method.facebook

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.modelfilter.dsl.where
import com.mashape.unirest.http.Unirest
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import studio.crud.feature.auth.authentication.method.base.AuthenticationMethodHandler
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.auth.authentication.method.facebook.model.FacebookAuthenticationPayloadDTO
import studio.crud.feature.auth.authentication.method.facebook.model.FacebookResponse
import studio.crud.feature.auth.authentication.method.facebook.model.FacebookResponsePojo
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.config.properties.authentication.FacebookAuthenticationProperties
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.auth.exception.FacebookTokenParsingFailedException
import studio.crud.feature.auth.exception.FacebookUidMismatchException
import studio.crud.sharedcommon.utils.GSON
import kotlin.reflect.KClass

@Component
@ConditionalOnProperty(prefix = FacebookAuthenticationProperties.PREFIX, name = ["enabled"], havingValue = "true")
class FacebookAuthenticationMethodHandlerImpl(
        private val crudHandler: CrudHandler
) : AuthenticationMethodHandler<FacebookAuthenticationPayloadDTO> {
    override val methodType: AuthenticationMethodType
        get() = AuthenticationMethodType.Facebook

    override val payloadType: KClass<FacebookAuthenticationPayloadDTO> = FacebookAuthenticationPayloadDTO::class

    override fun getUsernameFromPayload(payload: FacebookAuthenticationPayloadDTO): String {
        return payload.accessToken!!
    }

    override fun doLogin(payload: FacebookAuthenticationPayloadDTO, method: EntityAuthenticationMethod) {
        val facebookDTO = getFacebookDTO(payload.accessToken!!)
        if(facebookDTO.id != method.uid) {
            throw FacebookUidMismatchException()
        }
    }

    override fun doRegister(payload: FacebookAuthenticationPayloadDTO, params: CustomParamsDTO, entity: Entity): EntityAuthenticationMethod {
        val facebookDTO = getFacebookDTO(payload.accessToken!!)
        return EntityAuthenticationMethod(
                entity,
                AuthenticationMethodType.Facebook,
                facebookDTO.id,
                facebookDTO.email
        )
    }

    override fun getEntityMethod(payload: FacebookAuthenticationPayloadDTO): EntityAuthenticationMethod? {
        val facebookDTO = getFacebookDTO(payload.accessToken!!)
        return crudHandler.showBy(where {
            "param1" Equal facebookDTO.id
            "methodType" Equal  AuthenticationMethodType.Facebook
        }, EntityAuthenticationMethod::class.java)
                .execute()
    }

    override fun getUsername(method: EntityAuthenticationMethod): String {
        return method.email
    }

    private fun getFacebookDTO(accessToken: String): FacebookResponsePojo {
        val response = Unirest
                .get("https://graph.facebook.com/v8.0/me")
                .field("access_token", accessToken)
                .field("fields", "id,email")
                .asString()
        val result: FacebookResponse = GSON.fromJson(response.body, FacebookResponse::class.java)
        if (result.error != null) {
            throw FacebookTokenParsingFailedException(result.error.message ?: "Unknown")
        }
        if(result.email.isNullOrBlank()) {
            throw FacebookTokenParsingFailedException("Missing email in response")
        }

        return FacebookResponsePojo(result.email, result.id)
    }

    companion object {
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