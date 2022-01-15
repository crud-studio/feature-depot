package studio.crud.feature.core.web.model

import studio.crud.crudframework.ro.PagingRO
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable

class ResultRO<Payload>(
    var success: Boolean = true,
    var requestId: String? = null,

    @JsonIgnore
    @Transient
    var error: String? = null,
    var errorObject: ResultErrorDTO? = null,
    var result: Payload? = null,
    var paging: PagingRO? = null
) : Serializable