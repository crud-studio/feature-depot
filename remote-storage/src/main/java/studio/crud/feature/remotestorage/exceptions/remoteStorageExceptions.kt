package studio.crud.feature.remotestorage.exceptions

import studio.crud.feature.remotestorage.model.RemoteStorageValue
import studio.crud.sharedcommon.exception.AbstractResourceNotFoundByFieldException
import studio.crud.feature.exceptionhandling.core.ExceptionMetadata
import studio.crud.feature.exceptionhandling.core.ExceptionParam
import studio.crud.feature.exceptionhandling.core.ServerException

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