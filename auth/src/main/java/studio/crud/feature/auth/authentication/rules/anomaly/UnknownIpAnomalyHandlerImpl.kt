package studio.crud.feature.auth.authentication.rules.anomaly

import studio.crud.feature.auth.authentication.device.EntityDeviceHandler
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.authentication.rules.DeviceAnomalyType
import studio.crud.feature.auth.authentication.rules.anomaly.base.DeviceAnomalyHandler
import studio.crud.feature.audit.RequestSecurityMetadata
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UnknownIpAnomalyHandlerImpl : DeviceAnomalyHandler {

    @Autowired
    private lateinit var entityDeviceHandler: EntityDeviceHandler

    override val anomalyType: DeviceAnomalyType
        get() = DeviceAnomalyType.UNKNOWN_IP

    override fun handle(entity: Entity, securityMetadata: RequestSecurityMetadata): Boolean {
        val knownIps: List<String> = entityDeviceHandler.getEntityKnownIps(entity.id)
        if(knownIps.isNullOrEmpty()) {
            return false
        }

        if(securityMetadata.ip.isBlank()) {
            return true
        }

        return !knownIps.contains(securityMetadata.ip)
    }
}