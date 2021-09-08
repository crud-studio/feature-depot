package studio.crud.feature.core.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging
import studio.crud.feature.core.gson.DateLongFormatTypeAdapter
import studio.crud.feature.core.gson.NullableTypAdapterFactory
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

private val log = KotlinLogging.logger { }
private val VALIDATOR = Validation.buildDefaultValidatorFactory().validator

const val NULL_STRING = "null"
const val EMPTY_JSON_STRING = "{}"
val GSON: Gson = GsonBuilder()
    .serializeNulls()
    .registerTypeAdapterFactory(NullableTypAdapterFactory())
    .registerTypeAdapter(Date::class.java, DateLongFormatTypeAdapter())
    .create()

fun <PayloadType : Any> extractAndValidatePayload(payloadString: String?, payloadType: KClass<PayloadType>, vararg groups: Class<*>): PayloadType {

    val payload = try {
        log.debug { "Attempting to parse payload to type [ ${payloadType::class} ]" }
        val result = GSON.fromJson(payloadString, payloadType.java) ?: payloadType.createInstance()
        result
    } catch(e: Exception) {
        log.error(e) { "Failed to deserialize payload to type [ ${payloadType::class} ]" }
        throw PayloadDeserializationException(payloadString, payloadType)
    }
    validatePayload(payload, *groups)
    return payload
}

fun validatePayload(payload: Any, vararg groups: Class<*>) {
    // Validate with groups + without, for groupless 'catchall' validations
    val validation = VALIDATOR.validate(payload, *groups) + VALIDATOR.validate(payload)
    if(validation.isNotEmpty()) {
        throw PayloadValidationException(validation.map {
            KeyValuePair(it.propertyPath.toString(), it.message)
        })
    }
}

inline fun <reified T> Gson.fromJson(data: String?): T {
    val typeToken = object : TypeToken<T>(){}.type
    return this.fromJson<T>(data, typeToken)
}

fun Any?.toJson(): String {
    this ?: return EMPTY_JSON_STRING
    return GSON.toJson(this)
}