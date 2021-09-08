package studio.crud.feature.audit.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.Errors
import studio.crud.feature.core.config.ToggleableProperties
import studio.crud.feature.core.util.FEATURE_PROPERTY_PREFIX

@Configuration
@ConfigurationProperties(MaxmindProperties.PREFIX)
class MaxmindProperties : ToggleableProperties("Maxmind GeoIP Resolver") {

    /**
     * Path to the maxmind DB file, must be within the classpath
     */
    var dbPath: String = ""

    override fun validateOnEnabled(prefix: String, errors: Errors) {
        if(dbPath.isBlank()) {
            errors.rejectValue("dbPath", "field.required", "This property cannot be empty")
        }
    }

    companion object {
        const val PREFIX = "${FEATURE_PROPERTY_PREFIX}.maxmind-geoip-resolver"
    }
}