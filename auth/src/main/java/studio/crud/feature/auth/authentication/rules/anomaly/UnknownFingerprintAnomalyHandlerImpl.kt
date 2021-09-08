package studio.crud.feature.auth.authentication.rules.anomaly

import studio.crud.feature.auth.authentication.device.EntityDeviceHandler
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.authentication.rules.DeviceAnomalyType
import studio.crud.feature.auth.authentication.rules.anomaly.base.DeviceAnomalyHandler
import studio.crud.feature.core.audit.RequestSecurityMetadata
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UnknownFingerprintAnomalyHandlerImpl : DeviceAnomalyHandler {

    @Autowired
    private lateinit var entityDeviceHandler: EntityDeviceHandler

    override val anomalyType: DeviceAnomalyType
        get() = DeviceAnomalyType.UNKNOWN_FINGERPRINT

    override fun handle(entity: Entity, securityMetadata: RequestSecurityMetadata): Boolean {
        val knownFingerprints: List<String> = entityDeviceHandler.getEntityKnownFingerprints(entity.id)
        if(knownFingerprints.isNullOrEmpty()) {
            return false
        }

        return !knownFingerprints.contains(securityMetadata.fingerprint)
    }
}