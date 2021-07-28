package studio.crud.feature.auth.exception

import studio.crud.sharedcommon.exception.core.ExceptionMetadata
import studio.crud.sharedcommon.exception.core.ServerException

@ExceptionMetadata
class AuthenticatorInvalidCodeException : ServerException("Invalid code")