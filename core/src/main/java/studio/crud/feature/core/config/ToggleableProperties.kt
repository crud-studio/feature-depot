package studio.crud.feature.core.config

import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.validation.Errors

abstract class ToggleableProperties(
        val name: String,
        var enabled: Boolean = false
) : ValidatableProperties(), InitializingBean {
    override fun afterPropertiesSet() {
        if(enabled) {

        }
    }

    override fun validateInternal(prefix: String, errors: Errors) {
        if(enabled) {
            validateOnEnabled(prefix, errors)
        }
    }

    open fun validateOnEnabled(prefix: String, errors: Errors) {
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}