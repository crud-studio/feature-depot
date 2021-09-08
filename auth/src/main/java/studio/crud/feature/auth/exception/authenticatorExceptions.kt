package studio.crud.feature.auth.exception

import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.ServerException

@ExceptionMetadata
class AuthenticatorInvalidCodeException : ServerException("Invalid code")