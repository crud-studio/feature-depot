package studio.crud.feature.auth.exception

import studio.crud.sharedcommon.exception.core.ExceptionMetadata
import studio.crud.sharedcommon.exception.core.ServerException

@ExceptionMetadata
class CrudActionNotAllowedException : ServerException("Not allowed to perform this action")