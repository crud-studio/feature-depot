package studio.crud.feature.core.audit.filter

import org.apache.logging.log4j.ThreadContext
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import studio.crud.feature.core.util.generateFriendlyId
import studio.crud.feature.core.util.requestId
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * This filter adds a unique request ID to the request
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RequestIdFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val requestId = generateFriendlyId()
        ThreadContext.put("requestId", requestId)
        request.requestId = requestId
        chain.doFilter(request, response)
    }

    override fun getAlreadyFilteredAttributeName(): String {
        return this::class.java.name
    }
}