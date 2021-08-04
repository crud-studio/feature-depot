package studio.crud.feature.tracelog.model

import studio.crud.feature.tracelog.enums.TraceType
import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaEntity
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "trace_request")
class TraceRequest: AbstractJpaEntity() {

    @Enumerated
    @Column(name = "trace_type", nullable = false)
    var traceType: TraceType? = null

    @Column(name = "trace_target", nullable = false, length = 1023)
    var traceTarget: String? = null

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry", nullable = false)
    var expiry: Date? = null

}