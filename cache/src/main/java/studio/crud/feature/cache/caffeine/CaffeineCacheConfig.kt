package studio.crud.feature.cache.caffeine

import com.github.benmanes.caffeine.cache.Caffeine
import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import studio.crud.feature.core.cache.CacheDefinition
import studio.crud.feature.cache.config.properties.CacheProperties

@Configuration
@ConditionalOnProperty(prefix = CacheProperties.PREFIX, name = ["provider"], havingValue = "Caffeine", matchIfMissing = true)
class CaffeineCacheConfig(
    @Autowired(required = false)
    private val beanCacheDefinitions: List<CacheDefinition>,
    private val properties: CacheProperties
): InitializingBean {

    override fun afterPropertiesSet() {
        log.info { "Caffeine caching is active" }
    }

    @Bean
    @Primary
    fun caffeineCacheManager(): CaffeineCacheManager {
        val cacheManager = CaffeineCacheManager()
        val definitions = beanCacheDefinitions + properties.caches
        log.debug { "Found ${definitions.size} cache definition beans" }
        for (definition in definitions) {
            log.debug { "Registering cache definition ${definition.name}" }
            CaffeineCacheDefinitionValidator.validate(definition)
            val cache = Caffeine.newBuilder()
                .maximumSize(definition.maxSize!!)
                .expireAfterWrite(definition.ttl!!)
                .expireAfterAccess(definition.tti!!)
                .build<Any, Any>()
            cacheManager.registerCustomCache(definition.name, cache)
        }
        return cacheManager
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}