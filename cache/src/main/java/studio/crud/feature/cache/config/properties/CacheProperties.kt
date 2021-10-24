package studio.crud.feature.cache.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Configuration
import studio.crud.feature.core.cache.CacheDefinition
import studio.crud.feature.core.util.FEATURE_PROPERTY_PREFIX

@Configuration
@ConfigurationProperties(CacheProperties.PREFIX)
class CacheProperties {

    /**
     * Cache definitions
     */
    @NestedConfigurationProperty
    var caches: List<CacheDefinition> = emptyList()

    companion object {
        const val PREFIX = "$FEATURE_PROPERTY_PREFIX.cache"
    }
}

