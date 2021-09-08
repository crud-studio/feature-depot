package studio.crud.feature.auth.authentication.notifier.listener

import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.auth.model.ParsedStatelessToken
import studio.crud.feature.core.audit.RequestSecurityMetadata

interface LoginListener : AuthenticationListener{
    fun onLoginSuccess(body: String, method: EntityAuthenticationMethod, parsedToken: ParsedStatelessToken, securityMetadata: RequestSecurityMetadata) {}
    fun onLoginFailure(body: String, method: EntityAuthenticationMethod, securityMetadata: RequestSecurityMetadata, error: String) {}
}