package studio.crud.feature.auth.exception

import studio.crud.feature.core.exception.model.ExceptionMetadata
import studio.crud.feature.core.exception.model.ExceptionParam
import studio.crud.feature.core.exception.ServerException

abstract class NexmoException(message: String) : ServerException(message)

@ExceptionMetadata
class NexmoRequestNotFoundException : NexmoException("No request found")

@ExceptionMetadata(
    params = [
        ExceptionParam("secondsRemaining")
    ]
)
class NexmoCooldownException(val secondsRemaining: Long) : NexmoException("Please try again in $secondsRemaining seconds")

@ExceptionMetadata
class NexmoInvalidCodeException : NexmoException("Invalid code")


@ExceptionMetadata
class NexmoGeneralException(override val message: String) : NexmoException(message)