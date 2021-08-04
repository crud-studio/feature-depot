package studio.crud.feature.tracelog.model

import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "trace_log")
class TraceLog(): AbstractJpaEntity() {

    constructor(traceRequestId: Long?, principalUserName: String?, uri: String?, requestBody: String?, responseBody: String?) : this() {
        this.traceRequestId = traceRequestId
        this.principalUserName = principalUserName
        this.uri = uri
        this.requestBody = requestBody
        this.responseBody = responseBody
    }

    @Column(name = "trace_request_id", nullable = false)
    var traceRequestId: Long? = null

    @Column(name = "principal_user_name", nullable = true)
    var principalUserName: String? = null

    @Column(name = "uri", nullable = false, length = 511)
    var uri: String? = null

    @Column(name = "request_body", nullable = true, columnDefinition = "TEXT")
    var requestBody: String? = null

    @Column(name = "response_body", nullable = true, columnDefinition = "TEXT")
    var responseBody: String? = null

}