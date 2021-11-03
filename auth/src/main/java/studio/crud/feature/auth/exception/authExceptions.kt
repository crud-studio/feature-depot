package studio.crud.feature.auth.exception

import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.auth.authentication.mfa.enums.MfaType
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.core.exception.AbstractResourceNotFoundByIdException
import studio.crud.feature.core.exception.SecurityException
import studio.crud.feature.core.exception.ServerException
import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.annotation.ExceptionParam

@ExceptionMetadata
class EntityNotFoundException : ServerException("Entity not found")

@ExceptionMetadata
class EntityAlreadyExistsException : ServerException("Entity already exists")

@ExceptionMetadata(
    params = [
        ExceptionParam("entityId")
    ]
)
class EntityNotFoundByIdException(val entityId: Long) : AbstractResourceNotFoundByIdException(entityId, Entity::class.java.simpleName)

@ExceptionMetadata(
    params = [
        ExceptionParam("methodType")
    ]
)
class AuthenticationMethodNotSupportedException(val methodType: AuthenticationMethodType) : ServerException("Authentication method [ $methodType ] is not supported")

@ExceptionMetadata(
    params = [
        ExceptionParam("mfaType")
    ]
)
class MfaProviderNotSupportedException(val mfaType: MfaType) : ServerException("MFA provider [ $mfaType ] is not supported")

@ExceptionMetadata(
    params = [
        ExceptionParam("mfaType")
    ]
)
class MfaProviderNotConfiguredForEntityException(val mfaType: MfaType) : ServerException("MFA provider [ $mfaType ] is not configured")

@ExceptionMetadata(
    params = [
        ExceptionParam("mfaType")
    ]
)
class MfaProviderAlreadyConfiguredForEntityException(val mfaType: MfaType) : ServerException("MFA provider [ $mfaType ] is already configured")

@ExceptionMetadata(
    params = [
        ExceptionParam("mfaType")
    ]
)
class NoMfaSetupInProgressException(val mfaType: MfaType) : ServerException("No in progress setup for MFA provider [ $mfaType ]")



@ExceptionMetadata(
    params = [
        ExceptionParam("methodType")
    ]
)
class ForgotPasswordNotSupportedException(val methodType: AuthenticationMethodType) : ServerException("Forgot Password for [ $methodType ] is not supported")

@ExceptionMetadata(
    params = [
        ExceptionParam("methodType")
    ]
)
class AuthenticationMethodNotPasswordBasedException(val methodType: AuthenticationMethodType) : ServerException("Password operations are unavailable for type [ $methodType ]")

@ExceptionMetadata
class InvalidForgotPasswordTokenException : ServerException("Invalid Forgot Password token")

@ExceptionMetadata
class OldPasswordMismatchException : ServerException("Old password does not match")

@ExceptionMetadata
class InvalidTokenException: SecurityException("Invalid authentication token")

@ExceptionMetadata
class MfaRequiredException : SecurityException("MFA is required to proceed")

@ExceptionMetadata
class PasswordChangeRequiredException : SecurityException("Password Change is required to proceed")

@ExceptionMetadata
class InvalidEmailOrPasswordException : SecurityException("Invalid email/password")

@ExceptionMetadata
class EmailInUseException : SecurityException("Email in use")

@ExceptionMetadata
class TelephoneInUseException : SecurityException("Telephone in use")

@ExceptionMetadata(
    params = [
        ExceptionParam("mfaType")
    ]
)
class MfaIssueNotSupportedException(mfaType: MfaType) : ServerException("Issue is not supported for type [ $mfaType ]")

@ExceptionMetadata(
    params = [
        ExceptionParam("mfaType")
    ]
)
class MfaValidateNotSupportedException(mfaType: MfaType) : ServerException("Validate is not supported for type [ $mfaType ]")

@ExceptionMetadata
class RegistrationNotInitializedException : ServerException("Registration was not initialized")