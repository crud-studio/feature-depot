package studio.crud.feature.auth.exception

import studio.crud.feature.core.exception.model.ExceptionMetadata
import studio.crud.feature.core.exception.ServerException

@ExceptionMetadata
class GoogleUidMismatchException : ServerException("Google UID does not match")

@ExceptionMetadata
class GoogleIncorrectClientIdException : ServerException("Client ID does not match the server")

class GoogleTokenVerificationFailedException : ServerException("Failed to verify token")