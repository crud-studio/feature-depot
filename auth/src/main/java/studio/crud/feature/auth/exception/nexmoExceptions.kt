package studio.crud.feature.auth.exception

import studio.crud.sharedcommon.exception.core.ExceptionMetadata
import studio.crud.sharedcommon.exception.core.ExceptionParam
import studio.crud.sharedcommon.exception.core.ServerException

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