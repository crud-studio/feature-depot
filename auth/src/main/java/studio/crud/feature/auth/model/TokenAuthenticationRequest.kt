package studio.crud.feature.auth.model

import studio.crud.feature.auth.trait.annotations.AuthenticationTrait
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

data class TokenAuthenticationRequest(val token: String, val traits: List<AuthenticationTrait> = emptyList()) : Authentication {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }

    override fun getName(): String? {
        return null
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any? {
        return null
    }

    override fun isAuthenticated(): Boolean {
        return false
    }

    override fun getDetails(): Any? {
        return null
    }

}

