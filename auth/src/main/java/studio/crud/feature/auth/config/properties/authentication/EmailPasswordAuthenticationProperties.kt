package studio.crud.feature.auth.config.properties.authentication

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import studio.crud.feature.auth.config.properties.AUTHENTICATION_CONFIG_PREFIX
import studio.crud.feature.core.config.ToggleableProperties

@Configuration
@ConfigurationProperties(EmailPasswordAuthenticationProperties.PREFIX)
class EmailPasswordAuthenticationProperties: ToggleableProperties("Email Password Authentication") {
    var passwordRegex: String = ".*"
    var passwordExpiryDays: Long = 0

    companion object {
        const val PREFIX = "$AUTHENTICATION_CONFIG_PREFIX.email-password"
    }
}