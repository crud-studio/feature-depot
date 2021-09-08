package studio.crud.feature.auth.exception

import studio.crud.feature.core.exception.model.ExceptionMetadata
import studio.crud.feature.core.exception.ServerException

@ExceptionMetadata
class AuthenticatorInvalidCodeException : ServerException("Invalid code")