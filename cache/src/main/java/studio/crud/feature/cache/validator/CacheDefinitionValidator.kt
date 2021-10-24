package studio.crud.feature.cache.validator

import studio.crud.feature.core.cache.CacheDefinition

interface CacheDefinitionValidator {
    /**
     * Validate a cache definition
     */
    fun validate(definition: CacheDefinition)
}