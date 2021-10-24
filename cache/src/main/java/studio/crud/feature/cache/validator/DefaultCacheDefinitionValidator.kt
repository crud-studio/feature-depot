package studio.crud.feature.cache.validator

import studio.crud.feature.core.cache.CacheDefinition
import studio.crud.feature.cache.exception.InvalidCacheDefinitionException

open class DefaultCacheDefinitionValidator : CacheDefinitionValidator {
    override fun validate(definition: CacheDefinition) {
        if(definition.name.isBlank()) {
            throw InvalidCacheDefinitionException(definition, "Name cannot be empty")
        }
    }
}