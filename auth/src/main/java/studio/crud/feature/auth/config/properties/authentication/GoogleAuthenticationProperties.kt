package studio.crud.feature.auth.config.properties.authentication

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.Errors
import studio.crud.feature.auth.config.properties.AUTHENTICATION_CONFIG_PREFIX
import studio.crud.feature.core.config.ToggleableProperties

@Configuration
@ConfigurationProperties(GoogleAuthenticationProperties.PREFIX)
class GoogleAuthenticationProperties: ToggleableProperties("Google Authentication") {
        var clientId: String = ""

        override fun validateOnEnabled(prefix: String, errors: Errors) {
                if(clientId.isBlank()) {
                        errors.rejectValue("clientId", "field.required", "This property cannot be empty")
                }
        }

        companion object {
                const val PREFIX = "$AUTHENTICATION_CONFIG_PREFIX.google"
        }
}