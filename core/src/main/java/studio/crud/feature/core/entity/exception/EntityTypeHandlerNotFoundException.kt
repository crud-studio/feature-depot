package studio.crud.feature.core.entity.exception

import studio.crud.feature.core.exception.ServerException
import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.annotation.ExceptionParam

@ExceptionMetadata(
    params = [
        ExceptionParam("type")
    ]
)
class EntityTypeHandlerNotFoundException(val type: String) : ServerException("Entity Type Handler [ $type ] not found")

