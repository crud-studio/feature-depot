package studio.crud.feature.auth.exception

import studio.crud.feature.exceptionhandling.core.ExceptionMetadata
import studio.crud.feature.exceptionhandling.core.ExceptionParam
import studio.crud.feature.exceptionhandling.core.ServerException

@ExceptionMetadata
class FacebookUidMismatchException : ServerException("Facebook UID does not match")

@ExceptionMetadata(
    params = [
        ExceptionParam("reason")
    ]
)
class FacebookTokenParsingFailedException(val reason: String) : ServerException("Failed to retrieve information from the Facebook access token")