package studio.crud.feature.auth.authentication.device

import org.hibernate.Session
import org.springframework.stereotype.Repository
import studio.crud.feature.auth.authentication.device.enums.DeviceStatus
import studio.crud.feature.auth.authentication.device.model.EntityDevice
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class EntityDeviceDaoImpl(
    @PersistenceContext
    private val em: EntityManager
) : EntityDeviceDao {

    override fun saveOrUpdate(entityDevice: EntityDevice): EntityDevice {
        val session = em.unwrap(Session::class.java)
        session.saveOrUpdate(entityDevice)
        return entityDevice
    }

    override fun getEntityKnownIps(entityId: Long): List<String> {
        return em
                .createQuery("SELECT DISTINCT l.ip FROM EntityDevice l WHERE l.entityId = :entityId AND l.status = :status AND l.ip IS NOT NULL")
                .setParameter("status", DeviceStatus.Verified)
                .setParameter("entityId", entityId)
                .resultList as List<String>
    }

    override fun getEntityKnownCountryIsos(entityId: Long): List<String> {
        return em
                .createQuery("SELECT DISTINCT l.countryIso FROM EntityDevice l WHERE l.entityId = :entityId AND l.status = :status AND l.countryIso IS NOT NULL")
                .setParameter("status", DeviceStatus.Verified)
                .setParameter("entityId", entityId)
                .resultList as List<String>
    }

    override fun getEntityKnownUserAgents(entityId: Long): List<String> {
        return em
                .createQuery("SELECT DISTINCT l.userAgent FROM EntityDevice l WHERE l.entityId = :entityId AND l.status = :status AND l.userAgent IS NOT NULL")
                .setParameter("status", DeviceStatus.Verified)
                .setParameter("entityId", entityId)
                .resultList as List<String>
    }

    override fun getEntityKnownFingerprints(entityId: Long): List<String> {
        return em
                .createQuery("SELECT DISTINCT l.fingerprint FROM EntityDevice l WHERE l.entityId = :entityId AND l.status = :status AND l.fingerprint IS NOT NULL")
                .setParameter("status", DeviceStatus.Verified)
                .setParameter("entityId", entityId)
                .resultList as List<String>
    }

    override fun getValidDevicesAfterDate(entityId: Long, date: Date): List<EntityDevice> {
        return em
            .createQuery("SELECT ed from EntityDevice ed where ed.firstSeen >= :firstSeen and ed.status = :status and ed.entityId = :entityId")
            .setParameter("firstSeen", date)
            .setParameter("status", DeviceStatus.Invalid)
            .setParameter("entityId", entityId)
            .resultList as List<EntityDevice>
    }
}