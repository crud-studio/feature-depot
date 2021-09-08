package studio.crud.feature.core.web.model

import java.time.Instant

data class InstanceInfoDTO(
        val time: Instant? = null,
        val hostname: String? = null,
        val version: String? = null
)