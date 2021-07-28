package studio.crud.feature.auth.config.properties.mfa

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.Errors
import studio.crud.feature.auth.config.properties.MFA_CONFIG_PREFIX
import studio.crud.sharedcommon.config.ToggleableProperties

@Configuration
@ConfigurationProperties(NexmoMfaProperties.PREFIX)
class NexmoMfaProperties: ToggleableProperties("Nexmo MFA") {
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
                const val PREFIX = "$MFA_CONFIG_PREFIX.nexmo"
        }
}