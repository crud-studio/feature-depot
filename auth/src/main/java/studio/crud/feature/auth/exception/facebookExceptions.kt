package studio.crud.feature.auth.exception

import studio.crud.feature.core.exception.model.ExceptionMetadata
import studio.crud.feature.core.exception.model.ExceptionParam
import studio.crud.feature.core.exception.ServerException

@ExceptionMetadata
class FacebookUidMismatchException : ServerException("Facebook UID does not match")

@ExceptionMetadata(
    params = [
        ExceptionParam("reason")
    ]
)
class FacebookTokenParsingFailedException(val reason: String) : ServerException("Failed to retrieve information from the Facebook access token")