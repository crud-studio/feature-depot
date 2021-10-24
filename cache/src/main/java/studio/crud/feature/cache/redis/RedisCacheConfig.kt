package studio.crud.feature.cache.redis

import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import studio.crud.feature.core.cache.CacheDefinition
import studio.crud.feature.cache.config.properties.CacheProperties

@Configuration
@ConditionalOnProperty(prefix = CacheProperties.PREFIX, name = ["provider"], havingValue = "Redis")
class RedisCacheConfig(
    @Autowired(required = false)
    private val beanCacheDefinitions: List<CacheDefinition>,
    private val properties: CacheProperties
): InitializingBean {


    override fun afterPropertiesSet() {
        log.info { "Redis caching is active" }
    }

    @Bean
    @Primary
    fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        val builder = RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory)
        val definitions = beanCacheDefinitions + properties.caches
        log.debug { "Found ${definitions.size} cache definition beans" }
        for (definition in definitions) {
            log.debug { "Registering cache definition ${definition.name}" }
            RedisCacheDefinitionValidator.validate(definition)
            builder
                .withCacheConfiguration(
                    definition.name,
                    RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(definition.ttl!!)
                )
        }

        return builder.build()
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}