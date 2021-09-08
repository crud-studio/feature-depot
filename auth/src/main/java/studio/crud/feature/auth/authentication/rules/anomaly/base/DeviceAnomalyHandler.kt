package studio.crud.feature.auth.authentication.rules.anomaly.base

import com.antelopesystem.crudframework.utils.component.componentmap.annotation.ComponentMapKey
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.authentication.rules.DeviceAnomalyType
import studio.crud.feature.core.audit.RequestSecurityMetadata

interface DeviceAnomalyHandler {

    @get:ComponentMapKey
    val anomalyType: DeviceAnomalyType

    fun handle(entity: Entity, securityMetadata: RequestSecurityMetadata): Boolean
}