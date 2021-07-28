package studio.crud.feature.auth.authentication.mfa.provider.nexmo

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import studio.crud.feature.auth.authentication.mfa.enums.MfaType
import studio.crud.feature.auth.authentication.mfa.provider.base.MfaProvider
import studio.crud.feature.auth.authentication.mfa.provider.nexmo.model.NexmoMfaPayloadDTO
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.config.properties.mfa.NexmoMfaProperties
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.entity.model.EntityMfaMethod
import studio.crud.feature.auth.exception.NexmoInvalidCodeException
import studio.crud.feature.auth.integrations.nexmo.NexmoClient
import kotlin.reflect.KClass

@Component
@ConditionalOnProperty(prefix = NexmoMfaProperties.PREFIX, name = ["enabled"], havingValue = "true")
class NexmoMfaProvider(
    private val properties: NexmoMfaProperties
): MfaProvider<NexmoMfaPayloadDTO> {
    private val client by lazy { NexmoClient(properties.apiKey, properties.apiSecret, properties.brand) }
    override val type: MfaType = MfaType.Nexmo
    override val payloadType: KClass<NexmoMfaPayloadDTO> = NexmoMfaPayloadDTO::class

    override fun setup(payload: NexmoMfaPayloadDTO, entity: Entity): CustomParamsDTO {
        return CustomParamsDTO(
            payload.telephonePrefix!!,
            payload.telephone!!
        )
    }

    override fun issue(entity: Entity, params: CustomParamsDTO) {
        client.requestVerification(params.telephonePrefix + params.telephone)
    }

    override fun validate(code: String, entity: Entity, params: CustomParamsDTO) {
        val result = client.validateVerification(params.telephonePrefix + params.telephone, code)
        if (!result) {
            throw NexmoInvalidCodeException()
        }
    }

    companion object {

        /**
         * Extensions
         */
        private val CustomParamsDTO.telephonePrefix get(): String = this.param1

        private val CustomParamsDTO.telephone get(): String = this.param2

        private val EntityMfaMethod.telephonePrefix get(): String = this.param1!!

        private val EntityMfaMethod.telephone get(): String = this.param2!!
    }
}