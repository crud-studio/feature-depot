package studio.crud.feature.cache.caffeine

import mu.KotlinLogging
import studio.crud.feature.core.cache.CacheDefinition
import studio.crud.feature.cache.validator.DefaultCacheDefinitionValidator

object CaffeineCacheDefinitionValidator : DefaultCacheDefinitionValidator() {
    private val log = KotlinLogging.logger { }

    override fun validate(definition: CacheDefinition) {
        super.validate(definition)
        if(definition.maxSize == 0L) {
            log.warn { "Cache [ ${definition.name} ] defines maxSize as 0, which means Caffeine will instantly evict inserted entries" }
        }
    }
}