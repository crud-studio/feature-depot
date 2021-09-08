package studio.crud.feature.core.exception

import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.annotation.ExceptionParam
import studio.crud.feature.core.util.KeyValuePair
import studio.crud.feature.core.util.KeyValuePair.Companion.toPrettyString
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/* Abstracts */

abstract class InternalException(message: String) : ServerException(message) {
    init {
        overrideRepresentation(GeneralSystemException())
    }
}

abstract class SecurityException(message: String) : ServerException(message)

abstract class AbstractResourceFetchFailedException(resourceType: String, reason: String) : ServerException("Cannot fetch $resourceType. Reason: $reason")

abstract class AbstractResourceUpdateFailedException(resourceId: Long, resourceType: String, reason: String) : ServerException("Cannot update $resourceType with ID [ $resourceId ]. Reason: $reason")


abstract class AbstractResourceCreationFailedException(resourceType: String, reason: String) : ServerException("Creation of $resourceType failed. Reason: $reason")

abstract class AbstractResourceNotFoundByIdException(resourceId: Long?, resourceType: String) : AbstractResourceNotFoundByFieldException(
    resourceId.toString(),
    resourceType,
    "ID"
)

abstract class AbstractResourceNotFoundByUuidException(resourceUuid: String, resourceType: String) : AbstractResourceNotFoundByFieldException(
    resourceUuid,
    resourceType,
    "UUID"
)

abstract class AbstractResourceNotFoundByFieldException(value: String?, resourceType: String, fieldName: String) : ServerException(
    "$resourceType with $fieldName [ $value ] not found"
)

/* Exceptions */

@ExceptionMetadata
class GeneralSystemException : ServerException("An unknown error has occurred")

@ExceptionMetadata
class UnhandledException : ServerException(
    "Unhandled Exception"
) {
    companion object {
        val INSTANCE = UnhandledException()
    }
}

@ExceptionMetadata(
    params = [
        ExceptionParam("fields")
    ]
)
class FieldsNotUpdatableException(vararg val fields: String) : ServerException(
    "Fields [ ${fields.joinToString(", ")} ] are not updatable"
) {
    constructor(vararg properties: KProperty<*>) : this(*properties.map { it.name }.toTypedArray())
}

@ExceptionMetadata(
    params = [
        ExceptionParam("fields")
    ]
)
class RequiredFieldsMissingException(vararg val fields: String) : ServerException(
    "Fields [ ${fields.joinToString(", ")} ] are are required"
) {
    constructor(vararg properties: KProperty<*>) : this(*properties.map { it.name }.toTypedArray())
}

@ExceptionMetadata(
    params = [
        ExceptionParam("fields")
    ]
)
class RequiredFieldsMissingFromFilterException(vararg val fields: String) : ServerException(
    "Fields [ ${fields.joinToString(", ")} ] are required in this filter for the search to proceed"
) {
    constructor(vararg properties: KProperty<*>) : this(*properties.map { it.name }.toTypedArray())
}

@ExceptionMetadata(
    params = [
        ExceptionParam("fieldName"),
        ExceptionParam("value"),
        ExceptionParam("expectedValueType")
    ]
)
class BadFieldValueException(val fieldName: String, val value: String?, val expectedValueType: String?) : ServerException(
    "Bad value [ $value ] for field [ $fieldName ]. Expected value of type [ $expectedValueType ]"
)

@ExceptionMetadata
class RequestBodyMissingException: ServerException(
    "Request body is missing"
)

@ExceptionMetadata(
    params = [
        ExceptionParam("mediaType")
    ]
)
class UnsupportedMediaTypeException(val mediaType: String?) : ServerException(
    "Media type [ $mediaType ] is not supported"
)

@ExceptionMetadata(
    params = [
        ExceptionParam("httpMethod")
    ]
)
class UnsupportedHttpRequestMethodException(val httpMethod: String) : ServerException(
    "HTTP Method [ $httpMethod ] is not supported for this request"
)

@ExceptionMetadata
class RequestJsonParseException : ServerException(
    "Failed to parse request JSON"
)

@ExceptionMetadata(
    params = [
        ExceptionParam("payload"),
        ExceptionParam("payloadType")
    ]
)
class PayloadDeserializationException(val payload: String?, val payloadType: KClass<*>) : ServerException("Failed to deserialize [ $payload ] to [ $payloadType ]")

@ExceptionMetadata(
    params = [
        ExceptionParam("violations")
    ]
)
class PayloadValidationException(val violations: List<KeyValuePair<String, String>>) : ServerException("Payload validation failed - ${violations.map { it.toPrettyString() }.joinToString(",")}")