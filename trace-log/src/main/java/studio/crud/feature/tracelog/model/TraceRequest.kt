package studio.crud.feature.tracelog.model

import com.antelopesystem.crudframework.jpa.model.BaseJpaEntity
import studio.crud.feature.tracelog.enums.TraceType
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "trace_request")
class TraceRequest: BaseJpaEntity() {

    @Enumerated
    @get:Column(name = "trace_type", nullable = false)
    var traceType: TraceType? = null

    @get:Column(name = "trace_target", nullable = false, length = 1023)
    var traceTarget: String? = null

    @get:Temporal(TemporalType.TIMESTAMP)
    @get:Column(name = "expiry", nullable = false)
    var expiry: Date? = null

}