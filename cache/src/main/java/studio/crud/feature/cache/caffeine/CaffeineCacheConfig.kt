package studio.crud.feature.cache.caffeine

import com.github.benmanes.caffeine.cache.Caffeine
import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Configuration
import studio.crud.feature.cache.config.properties.CacheProperties
import studio.crud.feature.core.cache.CacheDefinition

@Configuration
@ConditionalOnProperty(prefix = "spring.cache", name = ["type"], havingValue = "CAFFEINE")
class CaffeineCacheConfig(
    @Autowired(required = false)
    private val beanCacheDefinitions: List<CacheDefinition>,
    private val properties: CacheProperties,
    private val cacheManager: CaffeineCacheManager
): InitializingBean {

    override fun afterPropertiesSet() {
        log.info { "Caffeine caching is active" }
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
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}