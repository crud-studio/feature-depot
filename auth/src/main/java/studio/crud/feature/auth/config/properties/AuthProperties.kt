package studio.crud.feature.auth.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.Errors
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.core.config.ValidatableProperties

@Configuration
@ConfigurationProperties(AuthProperties.PREFIX)
class AuthProperties : ValidatableProperties() {
    var tokenIssuer: String = "auth"

    var tokenLifetimeHours: Long = 24L

    var defaultAuthenticationMethodType: AuthenticationMethodType? = null

    var secret: String = ""

    override fun validateInternal(prefix: String, errors: Errors) {
        if(secret.isBlank()) {
            errors.rejectValue("secret", "field.required", "This property cannot be empty")
        }
    }

    companion object {
        const val PREFIX = ROOT_AUTH_PREFIX
    }
}