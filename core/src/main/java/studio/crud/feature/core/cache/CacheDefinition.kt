package studio.crud.feature.core.cache

import java.time.Duration


class CacheDefinition(
    /**
     * Cache name
     */
    var name: String = "",
    /**
     * The maximum amounts of elements in the cache
     */
    var maxSize: Long? = null,

    /**
     * The time after which an element expires after it has been inserted, 0 for a permanent cache
     */
    var ttl: Duration? = null,

    /**
     * The time after which an element expires after it has been accessed, 0 for a permanent cache
     */
    var tti: Duration? = null
)