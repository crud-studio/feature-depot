package studio.crud.feature.parameters.exception

import studio.crud.feature.core.exception.ServerException
import studio.crud.feature.core.exception.SystemConfigurationException
import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.annotation.ExceptionParam

@ExceptionMetadata(
    params = [
        ExceptionParam("parameterName")
    ]
)
class ParameterNotConfiguredException(val parameterName: String) : SystemConfigurationException("Parameter [ $parameterName ] is not configured")

@ExceptionMetadata(
    params = [
        ExceptionParam("parameterName"),
        ExceptionParam("reason")
    ]
)
class ParameterMisconfiguredException(val parameterName: String, val reason: String) : SystemConfigurationException("Parameter [ $parameterName ] is misconfigured: $reason")

@ExceptionMetadata
class InvalidParameterTypeException : ServerException("Invalid parameter type.")

@ExceptionMetadata(
    params = [
        ExceptionParam("value")
    ]
)
class InvalidParameterValueException(val value: String) : ServerException("Invalid parameter value [ $value ]")
