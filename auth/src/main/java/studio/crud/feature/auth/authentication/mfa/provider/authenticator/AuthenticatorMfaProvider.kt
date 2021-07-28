package studio.crud.feature.auth.authentication.mfa.provider.authenticator

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import studio.crud.feature.auth.authentication.mfa.enums.MfaType
import studio.crud.feature.auth.authentication.mfa.provider.authenticator.model.AuthenticatorMfaPayloadDTO
import studio.crud.feature.auth.authentication.mfa.provider.base.MfaProvider
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.config.properties.mfa.AuthenticatorMfaProperties
import studio.crud.feature.auth.entity.EntityHandler
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.entity.model.EntityMfaMethod
import studio.crud.feature.auth.exception.AuthenticatorInvalidCodeException
import studio.crud.feature.auth.integrations.authenticator.AuthenticatorClient
import kotlin.reflect.KClass

@Component
@ConditionalOnProperty(prefix = AuthenticatorMfaProperties.PREFIX, name = ["enabled"], havingValue = "true")
class AuthenticatorMfaProvider(
        private val entityHandler: EntityHandler,
        private val properties: AuthenticatorMfaProperties
) : MfaProvider<AuthenticatorMfaPayloadDTO> {
    private val client: AuthenticatorClient by lazy { AuthenticatorClient(properties.issuer) }

    override val type: MfaType = MfaType.Authenticator

    override val payloadType: KClass<AuthenticatorMfaPayloadDTO> = AuthenticatorMfaPayloadDTO::class

    override fun setup(payload: AuthenticatorMfaPayloadDTO, entity: Entity): CustomParamsDTO {
        val response = client.setup(entityHandler.getEntityUsername(entity))
        return CustomParamsDTO(
                response.key,
                response.keyUrl
        )
    }

    override fun validate(code: String, entity: Entity, params: CustomParamsDTO) {
        val result = client.validate(params.key, code.toInt())
        if(!result) {
            throw AuthenticatorInvalidCodeException()
        }
    }

    companion object {
        /**
         * Extensions
         */
        private val EntityMfaMethod.key get(): String = this.param1!!

        private val CustomParamsDTO.key get(): String = this.param1
    }
}