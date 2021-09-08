package studio.crud.feature.core.web.filter

import com.fasterxml.jackson.databind.ObjectMapper
import studio.crud.feature.core.exception.ClientExceptionTransformer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

abstract class AbstractExceptionHandlingFilter: OncePerRequestFilter() {

    @Autowired
    private lateinit var clientExceptionTransformer: ClientExceptionTransformer

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            doFilterInner(request, response, chain)
        } catch(e: Exception) {
            val error = when(e) {
                else -> e
            }

            val resultRO = clientExceptionTransformer.processExceptionForClient(error)
            response.status = 400
            response.writer.write(MAPPER.writeValueAsString(resultRO))
            response.addHeader("Content-Type", "application/json")
            return
        }
    }

    abstract fun doFilterInner(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain)

    final override fun getAlreadyFilteredAttributeName(): String {
        return this::class.java.name
    }

    companion object {
        private val MAPPER = ObjectMapper()
    }
}