package studio.crud.feature.parameters.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import studio.crud.feature.core.cache.CacheDefinition
import java.time.Duration

@Configuration
@ComponentScan("studio.crud.feature.parameters")
class ParameterFeatureAutoConfiguration {

    @Bean
    fun parameterCacheDefinition(): CacheDefinition {
        return CacheDefinition("parameterCache", 1200, Duration.ofMinutes(10), Duration.ofMinutes(10))
    }
}