package studio.crud.feature.auth.exception

import studio.crud.feature.core.exception.model.ExceptionMetadata
import studio.crud.feature.core.exception.ServerException

@ExceptionMetadata
class CrudActionNotAllowedException : ServerException("Not allowed to perform this action")