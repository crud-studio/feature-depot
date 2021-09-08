package studio.crud.feature.core.web.model

import com.fasterxml.jackson.annotation.JsonInclude
import studio.crud.feature.core.util.KeyValuePair
import java.io.Serializable

class ResultErrorDTO(
    val key: String,
    val message: String?,
    val params: Set<KeyValuePair<String, Serializable?>> = emptySet(),

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var stackTrace: String? = null
)