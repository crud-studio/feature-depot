package studio.crud.feature.cache.exception

import studio.crud.feature.core.cache.CacheDefinition
import studio.crud.feature.core.exception.InternalException
import studio.crud.feature.core.exception.annotation.ExceptionMetadata

@ExceptionMetadata
class InvalidCacheDefinitionException(
    definition: CacheDefinition,
    reason: String
) : InternalException("Invalid cache definition for ${definition.name}: $reason")