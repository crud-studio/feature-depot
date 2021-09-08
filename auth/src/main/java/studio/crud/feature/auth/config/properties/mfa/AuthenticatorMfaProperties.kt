package studio.crud.feature.auth.config.properties.mfa

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import studio.crud.feature.auth.config.properties.MFA_CONFIG_PREFIX
import studio.crud.feature.core.config.ToggleableProperties

@Configuration
@ConfigurationProperties(AuthenticatorMfaProperties.PREFIX)
class AuthenticatorMfaProperties: ToggleableProperties("Authenticator MFA") {
        var issuer: String = "Not configured"

        companion object {
                const val PREFIX = "$MFA_CONFIG_PREFIX.authenticator"
        }
}