package studio.crud.feature.auth.token.security.model

import org.springframework.security.authentication.AbstractAuthenticationToken

/**
 * Used to request an authentication request to the API
 * `token` represents the key's JWT token
 */
class ApiAuthenticationRequest(val token: String) : AbstractAuthenticationToken(null) {
    override fun getCredentials(): Any {
        return token
    }

    override fun getPrincipal(): Any? {
        return null
    }
}