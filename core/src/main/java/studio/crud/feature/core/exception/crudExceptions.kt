package studio.crud.feature.core.exception

import studio.crud.feature.core.crud.enums.CrudOperationType
import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.annotation.ExceptionParam
import kotlin.reflect.KClass

/* Abstracts */

abstract class AbstractCrudFacadeException(message: String?) : ServerException(
    message ?: ""
)

/* Exceptions */


@ExceptionMetadata
class ResourceCreationException(message: String?) : AbstractCrudFacadeException(message)

@ExceptionMetadata
class ResourceDeletionException(message: String?) : AbstractCrudFacadeException(message)

@ExceptionMetadata
class ResourceReadException(message: String?) : AbstractCrudFacadeException(message)

@ExceptionMetadata
class ResourceTransformationException(message: String?) : AbstractCrudFacadeException(message)

@ExceptionMetadata
class ResourceUpdateException(message: String?) : AbstractCrudFacadeException(message)

@ExceptionMetadata
class ResourceValidationException(message: String?) : AbstractCrudFacadeException(message)

@ExceptionMetadata(
    params = [
        ExceptionParam("crudOperationType")
    ]
)
class OperationNotSupportedForEntityException(val crudOperationType: CrudOperationType, entityClazz: KClass<*>) : ServerException(" [ " + crudOperationType.name + " ] operation for entity [ " + entityClazz.simpleName + " ] not supported")