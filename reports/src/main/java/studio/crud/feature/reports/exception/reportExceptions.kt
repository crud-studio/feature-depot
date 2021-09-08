package studio.crud.feature.reports.exception

import studio.crud.feature.core.exception.AbstractResourceNotFoundByIdException
import studio.crud.feature.core.exception.ServerException
import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.annotation.ExceptionParam
import studio.crud.feature.reports.model.Report

@ExceptionMetadata(
    params = [
        ExceptionParam("reportId")
    ]
)
class ReportNotFoundByIdException(val reportId: Long) : AbstractResourceNotFoundByIdException(reportId, Report::class.simpleName!!)


@ExceptionMetadata(
    params = [
        ExceptionParam("parameterName")
    ]
)
class ReportParameterInvalidException(val parameterName: String) : ServerException("Parameter [ $parameterName ] not found on report")

@ExceptionMetadata(
    params = [
        ExceptionParam("parameterName")
    ]
)
class ReportParameterMissingValueException(val parameterName: String) : ServerException("Parameter [ $parameterName ] is missing a value/default value")

@ExceptionMetadata(
    params = [
        ExceptionParam("parameterName"),
        ExceptionParam("value")
    ]
)
class ReportParameterInvalidValueException(val parameterName: String, val value: Any?) : ServerException("Value [ $value ] is not valid for parameter [ $parameterName ]")