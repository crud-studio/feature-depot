package studio.crud.feature.auth.authentication.method.nexmo

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.modelfilter.dsl.where
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import studio.crud.feature.auth.authentication.method.base.AuthenticationMethodHandler
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.auth.authentication.method.nexmo.model.NexmoAuthenticationPayloadDTO
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.config.properties.authentication.NexmoAuthenticationProperties
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.auth.exception.NexmoInvalidCodeException
import studio.crud.feature.auth.integrations.nexmo.NexmoClient
import kotlin.reflect.KClass

@Component
@ConditionalOnProperty(prefix = NexmoAuthenticationProperties.PREFIX, name = ["enabled"], havingValue = "true")
class NexmoAuthenticationMethodHandlerImpl(
        private val crudHandler: CrudHandler,
        private val properties: NexmoAuthenticationProperties
) : AuthenticationMethodHandler<NexmoAuthenticationPayloadDTO> {
    private val client by lazy { NexmoClient(properties.apiKey, properties.apiSecret, properties.brand, properties.codeLength, properties.bypassNumbers) }

    override val methodType: AuthenticationMethodType = AuthenticationMethodType.Nexmo

    override val payloadType: KClass<NexmoAuthenticationPayloadDTO> = NexmoAuthenticationPayloadDTO::class

    // todo cleanTelephone
    override fun getEntityMethod(payload: NexmoAuthenticationPayloadDTO): EntityAuthenticationMethod? {
        return crudHandler.showBy(where {
            "param1" Equal payload.telephonePrefix!!
            "param2" Equal payload.telephone!!
            "methodType" Equal  AuthenticationMethodType.Nexmo
        }, EntityAuthenticationMethod::class.java)
            .execute()
    }

    override fun getUsername(method: EntityAuthenticationMethod): String {
        return method.telephonePrefix + method.telephone
    }

    override fun getUsernameFromPayload(payload: NexmoAuthenticationPayloadDTO): String {
        return payload.telephonePrefix + payload.telephone
    }

    override fun initializeLogin(payload: NexmoAuthenticationPayloadDTO, method: EntityAuthenticationMethod): CustomParamsDTO {
        client.requestVerification(method.telephonePrefix + method.telephone)
        return CustomParamsDTO()
    }

    override fun doLogin(payload: NexmoAuthenticationPayloadDTO, method: EntityAuthenticationMethod) {
        val result = client.validateVerification(method.telephonePrefix + method.telephone, payload.code!!)
        if(!result) {
            throw NexmoInvalidCodeException()
        }
    }

    override fun initializeRegistration(payload: NexmoAuthenticationPayloadDTO): CustomParamsDTO {
        client.requestVerification(payload.telephonePrefix + payload.telephone)
        return CustomParamsDTO(
                payload.telephonePrefix!!,
                payload.telephone!!
        )
    }

    override fun doRegister(payload: NexmoAuthenticationPayloadDTO, params: CustomParamsDTO, entity: Entity): EntityAuthenticationMethod {
        val fullTelephone = payload.telephonePrefix + payload.telephone
        val result = client.validateVerification(fullTelephone, payload.code!!)
        if(!result) {
            throw NexmoInvalidCodeException()
        }

        val method = EntityAuthenticationMethod(entity, AuthenticationMethodType.Nexmo)
        method.telephonePrefix = payload.telephonePrefix!!
        method.telephone = payload.telephone!!
        return method
    }

    companion object {
        /**
         * Extensions
         */
        private var EntityAuthenticationMethod.telephonePrefix
            get() = this.param1!!
            set(value) {
                this.param1 = value
            }

        private var EntityAuthenticationMethod.telephone
            get() = this.param2!!
            set(value) {
                this.param2 = value
            }
    }
}