package studio.crud.feature.auth.config.properties.authentication

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import studio.crud.feature.auth.config.properties.AUTHENTICATION_CONFIG_PREFIX
import studio.crud.feature.core.config.ToggleableProperties

@Configuration
@ConfigurationProperties(FacebookAuthenticationProperties.PREFIX)
class FacebookAuthenticationProperties: ToggleableProperties("Facebook Authentication") {
        companion object {
                const val PREFIX = "$AUTHENTICATION_CONFIG_PREFIX.facebook"
        }
}