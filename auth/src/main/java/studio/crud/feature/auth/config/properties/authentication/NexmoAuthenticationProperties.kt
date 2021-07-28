package studio.crud.feature.auth.config.properties.authentication

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.Errors
import studio.crud.feature.auth.config.properties.AUTHENTICATION_CONFIG_PREFIX
import studio.crud.sharedcommon.config.ToggleableProperties

@Configuration
@ConfigurationProperties(NexmoAuthenticationProperties.PREFIX)
class NexmoAuthenticationProperties: ToggleableProperties("Nexmo Authentication") {
        var apiKey: String = ""
        var apiSecret: String = ""
        var brand: String = ""

        override fun validateOnEnabled(prefix: String, errors: Errors) {
                if(apiKey.isBlank()) {
                        errors.rejectValue("apiKey", "field.required", "This property cannot be empty")
                }

                if(apiSecret.isBlank()) {
                        errors.rejectValue("apiSecret", "field.required", "This property cannot be empty")
                }

                if(brand.isBlank()) {
                        errors.rejectValue("brand", "field.required", "This property cannot be empty")
                }
        }

        companion object {
                const val PREFIX = "$AUTHENTICATION_CONFIG_PREFIX.nexmo"
        }
}