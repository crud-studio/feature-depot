package studio.crud.feature.cache.redis

import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCache
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import studio.crud.feature.cache.config.properties.CacheProperties
import studio.crud.feature.core.cache.CacheDefinition

@Configuration
@ConditionalOnClass(RedisCache::class)
@ConditionalOnProperty(prefix = "spring.cache", name = ["type"], havingValue = "REDIS")
class RedisCacheConfig(
    @Autowired(required = false)
    private val beanCacheDefinitions: List<CacheDefinition>,
    private val properties: CacheProperties
): InitializingBean {


    override fun afterPropertiesSet() {
        log.info { "Redis caching is active" }
    }

    @Bean
    fun redisCacheManagerBuilderCustomizer(): RedisCacheManagerBuilderCustomizer {
        val customizer = RedisCacheManagerBuilderCustomizer { builder ->
            val definitions = beanCacheDefinitions + properties.caches
            val serializingPair = RedisSerializationContext.SerializationPair.fromSerializer(JdkSerializationRedisSerializer())
            log.debug { "Found ${definitions.size} cache definition beans" }
            for (definition in definitions) {
                log.debug { "Registering cache definition ${definition.name}" }
                RedisCacheDefinitionValidator.validate(definition)
                val cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(definition.ttl!!)
                    .serializeValuesWith(serializingPair)
                if(definition.allowNullValues != true) {
                    cacheConfiguration.disableCachingNullValues()
                }
                builder
                    .withCacheConfiguration(
                        definition.name,
                        cacheConfiguration
                    )
            }
        }
        return customizer
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}