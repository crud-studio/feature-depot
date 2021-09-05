package studio.crud.feature.auth.exception

import studio.crud.feature.exceptionhandling.core.ExceptionMetadata
import studio.crud.feature.exceptionhandling.core.ServerException

@ExceptionMetadata
class CrudActionNotAllowedException : ServerException("Not allowed to perform this action")