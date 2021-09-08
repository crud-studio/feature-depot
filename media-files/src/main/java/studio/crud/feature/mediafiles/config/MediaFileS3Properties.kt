package studio.crud.feature.mediafiles.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.Errors
import studio.crud.feature.core.config.ToggleableProperties
import studio.crud.feature.core.util.FEATURE_PROPERTY_PREFIX

@Configuration
@ConfigurationProperties(prefix = MediaFileS3Properties.PREFIX)
class MediaFileS3Properties : ToggleableProperties("MediaFile S3") {
    var bucket: String = ""

    override fun validateOnEnabled(prefix: String, errors: Errors) {
        if(bucket.isBlank()) {
            errors.rejectValue("bucket", "field.required", "This property cannot be empty")
        }
    }

    companion object {
        const val PREFIX = "$FEATURE_PROPERTY_PREFIX.mediafiles.s3"
    }
}