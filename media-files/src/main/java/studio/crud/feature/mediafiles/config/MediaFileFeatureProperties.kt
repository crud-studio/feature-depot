package studio.crud.feature.mediafiles.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.Errors
import studio.crud.feature.core.config.ValidatableProperties
import studio.crud.feature.core.util.FEATURE_PROPERTY_PREFIX

@Configuration
@ConfigurationProperties(prefix = MediaFileFeatureProperties.PREFIX)
class MediaFileFeatureProperties : ValidatableProperties() {
    /**
     * Base package to search for crud entities, used in association related operations
     */
    var basePackage: String = "studio.crud"

    override fun validateInternal(prefix: String, errors: Errors) {
        if(basePackage.isBlank()) {
            errors.rejectValue("basePackage", "field.required", "This property cannot be empty")
        }
    }

    companion object {
        const val PREFIX = "$FEATURE_PROPERTY_PREFIX.mediafiles"
    }
}