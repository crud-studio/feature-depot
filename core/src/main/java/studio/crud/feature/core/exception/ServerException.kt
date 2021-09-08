package studio.crud.feature.core.exception

import mu.KotlinLogging
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.util.ReflectionUtils
import studio.crud.feature.core.exception.model.ExceptionMetadata
import studio.crud.feature.core.util.KeyValuePair
import java.io.Serializable
import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

abstract class ServerException(
    message: String = ""
) : RuntimeException(message) {
    val params: MutableMap<String, Serializable?> by lazy {
        val map = mutableMapOf<String, Serializable?>()
        val fields = getParamFields(this::class)
        for (field in fields) {
            ReflectionUtils.makeAccessible(field)

            map[field.name] = try {
                field.get(this) as Serializable?
            } catch(e: Exception) {
                log.error("Failed to get field value for field [ ${field.name} ] for exception [ ${this::class.simpleName} ]")
                null
            }
        }
        map
    }

    var representationException: ServerException? = null

    fun overrideRepresentation(e: ServerException) : ServerException {
        this.representationException = e
        return this
    }

    fun getParamsAsPairs(): Set<KeyValuePair<String, Serializable?>> {
        return params.map { KeyValuePair(it.key, it.value) }.toSet()
    }

    companion object {
        val ServerException.displayName: String get() = this::class.java.simpleName
        private val log = KotlinLogging.logger { }
        private val paramCache = mutableMapOf<KClass<out ServerException>, List<Field>>()

        fun getParamNames(clazz: KClass<out ServerException>): Set<ExceptionParamDefinition> {
            return getParamFields(clazz).map { ExceptionParamDefinition(it.name, it.type.name) }.toSet()
        }

        private fun getParamFields(clazz: KClass<out ServerException>) = paramCache.computeIfAbsent(clazz) {
            val metadata = AnnotationUtils.findAnnotation(clazz.java, ExceptionMetadata::class.java)
            if (metadata == null || metadata.params.isEmpty()) {
                return@computeIfAbsent emptyList()
            }

            metadata.params
                .mapNotNull { param ->
                    val field = clazz.memberProperties.find { param.name == it.name }?.javaField
                    if(field == null) {
                        log.warn("Exception metadata param [ ${param.name} ] for exception [ ${clazz.simpleName} ] not found")
                    }
                    field
                }
        }
    }

    data class ExceptionParamDefinition(val name: String, val type: String)
}