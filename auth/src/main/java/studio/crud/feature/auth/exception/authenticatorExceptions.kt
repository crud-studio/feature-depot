package studio.crud.feature.auth.exception

import studio.crud.feature.exceptionhandling.core.ExceptionMetadata
import studio.crud.feature.exceptionhandling.core.ServerException

@ExceptionMetadata
class AuthenticatorInvalidCodeException : ServerException("Invalid code")