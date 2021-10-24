package studio.crud.feature.cache.config

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("studio.crud.feature.cache")
@EnableCaching
class CacheFeatureAutoConfiguration

