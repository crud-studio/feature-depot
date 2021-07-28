package studio.crud.feature.auth.authentication.rules

import studio.crud.feature.auth.authentication.device.EntityDeviceHandler
import studio.crud.feature.auth.authentication.device.model.EntityDevice
import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.auth.authentication.notifier.listener.LoginListener
import studio.crud.feature.auth.token.metadata.TokenMetadataHandler
import studio.crud.feature.auth.model.ParsedStatelessToken
import studio.crud.sharedcommon.audit.RequestSecurityMetadata
import org.springframework.stereotype.Component

@Component
class DeviceListener(
        private val authenticationValidator: AuthenticationValidator,
        private val entityDeviceHandler: EntityDeviceHandler,
        private val tokenMetadataHandler: TokenMetadataHandler
) : LoginListener {
    override fun onLoginSuccess(body: String, method: EntityAuthenticationMethod, parsedToken: ParsedStatelessToken, securityMetadata: RequestSecurityMetadata) {
        val device = entityDeviceHandler.createOrUpdateDevice(EntityDevice(method.entity.id, securityMetadata))
        tokenMetadataHandler.updateTokenMetadata(parsedToken) {
            it.deviceHash = device.hash
            it.deviceScore = authenticationValidator.validate(method.entity, securityMetadata)
        }
    }
}