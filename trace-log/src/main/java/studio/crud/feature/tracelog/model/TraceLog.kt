package studio.crud.feature.tracelog.model

import com.antelopesystem.crudframework.jpa.model.BaseJpaEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "trace_log")
class TraceLog(): BaseJpaEntity() {

    constructor(traceRequestId: Long?, principalUserName: String?, uri: String?, requestBody: String?, responseBody: String?) : this() {
        this.traceRequestId = traceRequestId
        this.principalUserName = principalUserName
        this.uri = uri
        this.requestBody = requestBody
        this.responseBody = responseBody
    }

    @get:Column(name = "trace_request_id", nullable = false)
    var traceRequestId: Long? = null

    @get:Column(name = "principal_user_name", nullable = true)
    var principalUserName: String? = null

    @get:Column(name = "uri", nullable = false, length = 511)
    var uri: String? = null

    @get:Column(name = "request_body", nullable = true, columnDefinition = "TEXT")
    var requestBody: String? = null

    @get:Column(name = "response_body", nullable = true, columnDefinition = "TEXT")
    var responseBody: String? = null

}