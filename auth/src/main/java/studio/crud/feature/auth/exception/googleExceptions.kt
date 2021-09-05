package studio.crud.feature.auth.exception

import studio.crud.feature.exceptionhandling.core.ExceptionMetadata
import studio.crud.feature.exceptionhandling.core.ServerException

@ExceptionMetadata
class GoogleUidMismatchException : ServerException("Google UID does not match")

@ExceptionMetadata
class GoogleIncorrectClientIdException : ServerException("Client ID does not match the server")

class GoogleTokenVerificationFailedException : ServerException("Failed to verify token")