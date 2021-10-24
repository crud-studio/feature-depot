package studio.crud.feature.cache.redis

import mu.KotlinLogging
import studio.crud.feature.core.cache.CacheDefinition
import studio.crud.feature.cache.validator.DefaultCacheDefinitionValidator

object RedisCacheDefinitionValidator : DefaultCacheDefinitionValidator() {
    private val log = KotlinLogging.logger { }

    override fun validate(definition: CacheDefinition) {
        super.validate(definition)
        if(definition.tti != null) {
            log.warn { "Cache [ ${definition.name} ] defines TTI which is not supported by Redis" }
        }
        if(definition.maxSize != null) {
            log.warn { "Cache [ ${definition.name} ] defines maxSize which is not supported by Redis" }
        }
    }
}