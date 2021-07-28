package studio.crud.feature.auth.exception

import studio.crud.sharedcommon.exception.core.ExceptionMetadata
import studio.crud.sharedcommon.exception.core.ExceptionParam
import studio.crud.sharedcommon.exception.core.ServerException

@ExceptionMetadata
class FacebookUidMismatchException : ServerException("Facebook UID does not match")

@ExceptionMetadata(
    params = [
        ExceptionParam("reason")
    ]
)
class FacebookTokenParsingFailedException(val reason: String) : ServerException("Failed to retrieve information from the Facebook access token")