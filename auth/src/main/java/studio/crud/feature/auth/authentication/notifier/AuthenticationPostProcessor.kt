package studio.crud.feature.auth.authentication.notifier

import studio.crud.feature.auth.authentication.model.ForgotPasswordToken
import studio.crud.feature.auth.authentication.model.MethodRequestPayload
import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.auth.model.ParsedStatelessToken

interface AuthenticationPostProcessor {
    fun onLoginSuccess(body: String, method: EntityAuthenticationMethod, parsedToken: ParsedStatelessToken)
    fun onLoginFailure(body: String, method: EntityAuthenticationMethod, error: String)
    fun onRegistrationSuccess(body: String, method: EntityAuthenticationMethod, parsedToken: ParsedStatelessToken)
    fun onRegistrationFailure(body: String, error: String)
    fun onForgotPasswordInitialized(token: String, method: EntityAuthenticationMethod)
    fun onForgotPasswordSuccess(token: ForgotPasswordToken, method: EntityAuthenticationMethod)
}