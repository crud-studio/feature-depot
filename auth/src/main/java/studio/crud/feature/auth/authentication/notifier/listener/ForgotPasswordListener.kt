package studio.crud.feature.auth.authentication.notifier.listener

import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.auth.authentication.model.ForgotPasswordToken
import studio.crud.feature.core.audit.RequestSecurityMetadata

interface ForgotPasswordListener : AuthenticationListener {
    fun onForgotPasswordInitialized(token: String, method: EntityAuthenticationMethod, securityMetadata: RequestSecurityMetadata) {}
    fun onForgotPasswordSuccess(token: ForgotPasswordToken, method: EntityAuthenticationMethod, securityMetadata: RequestSecurityMetadata) {}
}