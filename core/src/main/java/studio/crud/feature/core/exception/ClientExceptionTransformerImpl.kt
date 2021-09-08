package studio.crud.feature.core.exception

import com.antelopesystem.crudframework.crud.exception.*
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.util.NestedServletException
import studio.crud.feature.core.audit.RequestSecurityMetadataProvider
import studio.crud.feature.core.exception.ServerException.Companion.displayName
import studio.crud.feature.core.web.model.ResultErrorDTO
import studio.crud.feature.core.web.model.ResultRO
import java.lang.reflect.UndeclaredThrowableException
import kotlin.reflect.jvm.jvmName

@Component
class ClientExceptionTransformerImpl(
    val requestSecurityMetadataProvider: RequestSecurityMetadataProvider,
    @Value("\${app.exceptions.show-stack-trace:false}") val showStackTrace: Boolean
) : ClientExceptionTransformer {
    override fun processExceptionForClient(e: Exception): ResultRO<*> {
        val clientMetadata = requestSecurityMetadataProvider.get()
        val resultRO = ResultRO<Nothing>(success = false, requestId = clientMetadata.requestId)
        val unwrappedException = unwrapAndTranslateCrudException(e)
        if (unwrappedException is ServerException) {
            val representation = unwrappedException.representationException ?: unwrappedException
            resultRO.errorObject = ResultErrorDTO(
                representation.displayName,
                representation.message,
                representation.getParamsAsPairs()
            )
            resultRO.error = representation.message
            val errorObject = resultRO.errorObject!!
            val errorKey = if (unwrappedException == representation) {
                unwrappedException.displayName
            } else {
                "${unwrappedException.displayName} (${errorObject.key})"
            }

            clientExceptionLog.info("<${clientMetadata.requestId}> $errorKey: ${errorObject.message} ${errorObject.params} -> metadata: $clientMetadata")
        } else {
            val unhandledException = UnhandledException()
            resultRO.errorObject = ResultErrorDTO(
                unhandledException::class.java.simpleName,
                unhandledException.message
            )
            resultRO.error = unhandledException.message
            clientExceptionLog.error("<${clientMetadata.requestId}> (Unhandled) ${unwrappedException::class.simpleName!!}: ${unwrappedException.message} -> metadata: $clientMetadata", e)
        }

        if(showStackTrace) {
            resultRO.errorObject!!.stackTrace = ExceptionUtils.getStackTrace(e)
        }

        return resultRO
    }

    private fun unwrapAndTranslateCrudException(e: Exception): Throwable {
        if(e !is CrudException) {
            return translateException(e)
        }
        var cause = e.cause
        while (cause != null) {
            if (cause is ServerException) {
                return translateException(cause)
            }
            cause = cause.cause
        }
        return translateCrudException(e)
    }

    private fun translateException(e: Exception): Throwable {
        var actualException = when (e) {
            is MissingServletRequestParameterException -> RequiredFieldsMissingException(e.parameterName)
            is MethodArgumentTypeMismatchException -> BadFieldValueException(e.name, e.value?.toString(), e.requiredType?.name)
            is HttpMessageNotReadableException -> {
                val message = e.message ?: ""
                when {
                    message.startsWith("Required request body is missing") -> RequestBodyMissingException()
                    message.startsWith("JSON parse error") -> RequestJsonParseException()
                    else -> e
                }
            }
            is HttpMediaTypeNotSupportedException -> UnsupportedMediaTypeException(e.contentType.toString())
            is HttpRequestMethodNotSupportedException -> UnsupportedHttpRequestMethodException(e.method)
            is UndeclaredThrowableException -> e.undeclaredThrowable
            is NestedServletException -> e.cause ?: e
            else -> e
        }

        actualException = when(actualException::class.jvmName) {
            "org.springframework.security.authentication.InternalAuthenticationServiceException" -> actualException.cause as java.lang.Exception? ?: actualException
            "org.springframework.security.access.AccessDeniedException" -> UnauthorizedException()
            else -> actualException
        }

        // Spring wraps exceptions thrown within the authentication flow with this exception. Attempt to unwrap
        if(actualException::class.jvmName == "org.springframework.security.authentication.InternalAuthenticationServiceException") {
            actualException = actualException.cause as java.lang.Exception? ?: actualException
        }

        return actualException
    }

    private fun translateCrudException(e: CrudException): Exception {
        return when (e) {
            is CrudCreateException -> ResourceCreationException(e.message)
            is CrudDeleteException -> ResourceDeletionException(e.message)
            is CrudReadException -> ResourceReadException(e.message)
            is CrudTransformationException -> ResourceTransformationException(e.message)
            is CrudUpdateException -> ResourceUpdateException(e.message)
            is CrudValidationException -> ResourceValidationException(e.message)
            else -> e
        }
    }

    companion object {
        private val clientExceptionLog = LogFactory.getLog("clientException")
    }
}