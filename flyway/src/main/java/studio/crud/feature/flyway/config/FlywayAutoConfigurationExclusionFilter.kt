package studio.crud.feature.flyway.config

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata

/**[]
 * Excludes the automatic flyway configuration from the project as a whole
 */
class FlywayAutoConfigurationExclusionFilter : AutoConfigurationImportFilter {
    override fun match(classNames: Array<String>, metadata: AutoConfigurationMetadata): BooleanArray {
        val matches = BooleanArray(classNames.size)
        for (i in classNames.indices) {
            matches[i] = FLYWAY_AUTOCONFIGURATION_CLASS_NAME != classNames[i]
        }
        return matches
    }

    companion object {
        private const val FLYWAY_AUTOCONFIGURATION_CLASS_NAME = "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
    }
}
