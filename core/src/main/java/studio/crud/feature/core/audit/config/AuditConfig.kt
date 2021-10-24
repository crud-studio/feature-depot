package studio.crud.feature.core.audit.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import studio.crud.feature.core.cache.CacheDefinition
import java.time.Duration

@Configuration
class AuditConfig {
    @Bean
    fun parameterCacheDefinition(): CacheDefinition {
        return CacheDefinition("geoIpResolutionCache", 1200, Duration.ofMinutes(10), Duration.ofMinutes(10))
    }

    @Bean
    fun hostnameResolutionCache(): CacheDefinition {
        return CacheDefinition("hostnameResolutionCache", 1200, Duration.ofMinutes(10), Duration.ofMinutes(10))
    }
}