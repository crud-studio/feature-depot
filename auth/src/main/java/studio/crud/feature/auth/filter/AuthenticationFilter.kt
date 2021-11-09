package studio.crud.feature.auth.filter

import org.slf4j.MDC
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import studio.crud.feature.auth.model.TokenAuthentication
import studio.crud.feature.auth.model.TokenAuthenticationRequest
import studio.crud.feature.auth.trait.RequestAuthenticationTraitResolver
import studio.crud.feature.core.web.filter.AbstractExceptionHandlingFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val requestAuthenticationTraitResolver: RequestAuthenticationTraitResolver
) : AbstractExceptionHandlingFilter() {
    override fun doFilterInner(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val token = request.getToken()
        if(!token.isNullOrBlank()) {
            val traits = requestAuthenticationTraitResolver.resolve(request)
            val request = TokenAuthenticationRequest(token, traits)
            val authentication = authenticationManager.authenticate(request) as TokenAuthentication
            SecurityContextHolder.getContext().authentication = authentication
            MDC.put("authEntityUuid", authentication.userInfo.entityUuid)
        }
        chain.doFilter(request, response)
    }

    private fun HttpServletRequest.getToken(): String? {
        return this.getHeader(TOKEN_HEADER)
    }

    companion object {
        private const val TOKEN_HEADER = "x-token"
    }
}