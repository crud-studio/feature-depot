package studio.crud.feature.auth.model

import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

class TokenAuthentication(val userInfo: UserInfo) : Authentication {
    val grants by lazy {
        userInfo.grants.map { SimpleGrantedAuthority(it) }
    }

    override fun getName(): String {
        return userInfo.entityUuid
    }

    override fun getAuthorities(): List<SimpleGrantedAuthority> {
        return grants
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any {
        return userInfo
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun getDetails(): Any? {
        return null
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }
}