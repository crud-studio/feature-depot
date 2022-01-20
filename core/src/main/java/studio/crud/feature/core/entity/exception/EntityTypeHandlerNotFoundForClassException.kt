package studio.crud.feature.core.entity.exception

import studio.crud.feature.core.exception.ServerException
import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.annotation.ExceptionParam

@ExceptionMetadata(
    params = [
        ExceptionParam("clazzName")
    ]
)
class EntityTypeHandlerNotFoundForClassException(val clazzName: String) : ServerException("Entity Type Handler for class [ $clazzName ] not found")