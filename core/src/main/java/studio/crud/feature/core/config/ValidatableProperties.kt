package studio.crud.feature.core.config

import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.validation.annotation.Validated

/**
 * Allows validation of configuration properties
 */
@Validated
abstract class ValidatableProperties : Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return this::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        validateInternal(errors.objectName, errors)
    }

    /**
     * Use to validate fields
     * @param prefix The full prefix to the current ConfigurationProperties class
     * @param errors The [Errors] object provided by Spring
     */
    fun validateInternal(prefix: String, errors: Errors) {}
}