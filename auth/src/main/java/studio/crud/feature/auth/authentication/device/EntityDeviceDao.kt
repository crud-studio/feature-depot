package studio.crud.feature.auth.authentication.device

import studio.crud.feature.auth.authentication.device.model.EntityDevice
import java.util.*

interface EntityDeviceDao {
    fun getEntityKnownIps(entityId: Long): List<String>

    fun getEntityKnownCountryIsos(entityId: Long): List<String>

    fun getEntityKnownUserAgents(entityId: Long): List<String>

    fun getEntityKnownFingerprints(entityId: Long): List<String>

    fun getValidDevicesAfterDate(entityId: Long, date: Date): List<EntityDevice>
    fun saveOrUpdate(entityDevice: EntityDevice): EntityDevice
}