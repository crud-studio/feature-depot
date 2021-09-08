package studio.crud.feature.remotestorage.exceptions

import studio.crud.feature.core.exception.AbstractResourceNotFoundByFieldException
import studio.crud.feature.core.exception.ServerException
import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.annotation.ExceptionParam
import studio.crud.feature.remotestorage.model.RemoteStorageValue

@ExceptionMetadata
class CouldNotDetermineValueOwnerException : ServerException("Could not determine value owner")

@ExceptionMetadata(
    params = [
        ExceptionParam("identifier")
    ]
)
class RemoteStorageValueNotFoundByIdentifierException(
    val identifier: String
) : AbstractResourceNotFoundByFieldException(identifier, RemoteStorageValue::class.simpleName!!, "identifier")