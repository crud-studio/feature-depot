package studio.crud.feature.auth.authentication.notifier

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import studio.crud.feature.auth.authentication.model.ForgotPasswordToken
import studio.crud.feature.auth.authentication.notifier.listener.ForgotPasswordListener
import studio.crud.feature.auth.authentication.notifier.listener.LoginListener
import studio.crud.feature.auth.authentication.notifier.listener.RegistrationListener
import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.auth.model.ParsedStatelessToken
import studio.crud.feature.core.audit.RequestSecurityMetadataProvider

@Component
class AuthenticationPostProcessorImpl(
        @Autowired(required=false) private val loginListeners: List<LoginListener> = listOf(),
        @Autowired(required=false) private val registrationListeners: List<RegistrationListener> = listOf(),
        @Autowired(required=false) private val forgotPasswordListeners: List<ForgotPasswordListener> = listOf(),
        private val securityMetadataProvider: RequestSecurityMetadataProvider
) : AuthenticationPostProcessor {
    override fun onLoginSuccess(body: String, method: EntityAuthenticationMethod, parsedToken: ParsedStatelessToken) {
        loginListeners.forEach {
            it.onLoginSuccess(body, method, parsedToken, securityMetadataProvider.get())
        }
    }

    override fun onLoginFailure(body: String, method: EntityAuthenticationMethod, error: String) {
        loginListeners.forEach {
            it.onLoginFailure(body, method, securityMetadataProvider.get(), error)
        }
    }

    override fun onRegistrationSuccess(body: String, method: EntityAuthenticationMethod, parsedToken: ParsedStatelessToken) {
        registrationListeners.forEach {
            it.onRegistrationSuccess(body, method, parsedToken, securityMetadataProvider.get())
        }

    }

    override fun onRegistrationFailure(body: String, error: String) {
        registrationListeners.forEach {
            it.onRegistrationFailure(body, securityMetadataProvider.get(), error)
        }
    }

    override fun onForgotPasswordInitialized(token: String, method: EntityAuthenticationMethod) {
        forgotPasswordListeners.forEach {
            it.onForgotPasswordInitialized(token, method, securityMetadataProvider.get())
        }
    }

    override fun onForgotPasswordSuccess(token: ForgotPasswordToken, method: EntityAuthenticationMethod) {
        forgotPasswordListeners.forEach {
            it.onForgotPasswordSuccess(token, method, securityMetadataProvider.get())
        }
    }
}