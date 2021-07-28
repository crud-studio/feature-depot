package studio.crud.feature.auth.authentication.device.model

import studio.crud.feature.auth.authentication.device.enums.DeviceStatus
import studio.crud.feature.auth.util.hashSHA256
import studio.crud.sharedcommon.audit.RequestSecurityMetadata
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "auth_entity_device")
class EntityDevice(
        @Column
        var entityId: Long,

        @Column
        var userAgent: String? = null,

        @Column
        var ip: String? = null,

        @Column
        var fingerprint: String? = null,

        @Column
        var countryIso: String,

        @Enumerated
        var status: DeviceStatus = DeviceStatus.Unknown,

        @Column(updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        var firstSeen: Date = Date(),

        @Temporal(TemporalType.TIMESTAMP)
        var lastSeen: Date = Date(),

        @Id
        var hash: String = hashSHA256(entityId.toString() + userAgent.toString() + ip.toString() + fingerprint.toString() + countryIso)

) : Serializable {
    constructor(entityId: Long, securityMetadata: RequestSecurityMetadata) : this(entityId, securityMetadata.userAgent, securityMetadata.ip, securityMetadata.fingerprint, securityMetadata.countryIso, DeviceStatus.Verified)
}

