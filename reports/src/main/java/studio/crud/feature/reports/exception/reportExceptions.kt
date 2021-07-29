package studio.crud.feature.reports.exception

import studio.crud.feature.reports.model.Report
import studio.crud.sharedcommon.exception.AbstractResourceNotFoundByIdException
import studio.crud.sharedcommon.exception.core.ExceptionMetadata
import studio.crud.sharedcommon.exception.core.ExceptionParam
import studio.crud.sharedcommon.exception.core.ServerException

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