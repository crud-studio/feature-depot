package studio.crud.feature.auth

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import studio.crud.feature.auth.exception.InvalidTokenException
import studio.crud.sharedcommon.web.ClientExceptionTransformer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Class that handle unauthorized request to REST service.
 */
@Component
class RestAuthenticationEntryPoint(
    private val clientExceptionTransformer: ClientExceptionTransformer
) : AuthenticationEntryPoint {
    override fun commence(request: HttpServletRequest, response: HttpServletResponse,
                          e: AuthenticationException) {
        val resultRO = clientExceptionTransformer.processExceptionForClient(InvalidTokenException())
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.writer.write(mapper.writeValueAsString(resultRO))
        response.addHeader("Content-Type", "application/json")
        return
    }

    private val mapper = ObjectMapper()
}