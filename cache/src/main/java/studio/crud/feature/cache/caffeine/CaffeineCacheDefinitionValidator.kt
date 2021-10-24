package studio.crud.feature.cache.caffeine

import mu.KotlinLogging
import studio.crud.feature.cache.validator.DefaultCacheDefinitionValidator
import studio.crud.feature.core.cache.CacheDefinition

object CaffeineCacheDefinitionValidator : DefaultCacheDefinitionValidator() {
    private val log = KotlinLogging.logger { }

    override fun validate(definition: CacheDefinition) {
        super.validate(definition)
        if(definition.maxSize == 0L) {
            log.warn { "Cache [ ${definition.name} ] defines maxSize as 0, which means Caffeine will instantly evict inserted entries" }
        }
        if(definition.allowNullValues == false) {
            log.warn { "Cache [ ${definition.name} ] defines allowNullValues as false, this property defaults to true in Caffeine and cannot be changed" }
        }

        if(definition.allowNullValues == null) {
            log.warn { "Cache [ ${definition.name} ] does not define allowNullValues, this property defaults to true in Caffeine" }
        }
    }
}