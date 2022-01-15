package studio.crud.feature.parameters

import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.modelfilter.dsl.where
import org.springframework.stereotype.Component
import studio.crud.feature.parameters.exception.ParameterMisconfiguredException
import studio.crud.feature.parameters.exception.ParameterNotConfiguredException
import studio.crud.feature.parameters.model.Parameter

/**
 * {@inheritDoc}
 */
@Component("parameterHandler")
class ParameterHandlerImpl(
        private val crudHandler: CrudHandler
) : ParameterHandler {
    override fun getStringParameter(name: String, defaultValue: String?): String? {
        val parameter = crudHandler.showBy(where {
            "name" Equal name
        }, Parameter::class.java)
                .fromCache()
                .execute()
        return parameter?.value ?: defaultValue
    }

    override fun getBooleanParameter(name: String, defaultValue: Boolean): Boolean {
        val value = getStringParameter(name, defaultValue.toString())
        return try {
            java.lang.Boolean.parseBoolean(value)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override fun getIntegerParameter(name: String, defaultValue: Int): Int {
        val value = getStringParameter(name, defaultValue.toString())
        return try {
            value!!.toInt()
        } catch (e: Exception) {
            defaultValue
        }
    }

    override fun getDoubleParameter(name: String, defaultValue: Double): Double {
        val value = getStringParameter(name, defaultValue.toString())
        return try {
            value!!.toDouble()
        } catch (e: Exception) {
            defaultValue
        }
    }

    override fun getStringParameter(name: String): String {
        val parameter = crudHandler.showBy(where {
            "name" Equal name
        }, Parameter::class.java)
            .fromCache()
            .execute()
        return parameter?.value ?: throw ParameterNotConfiguredException(name)
    }

    override fun getBooleanParameter(name: String): Boolean {
        val value = getStringParameter(name)
        return try {
            value.toBoolean()
        } catch (e: NumberFormatException) {
            throw ParameterMisconfiguredException(name, "Boolean parsing failed")
        }
    }

    override fun getIntegerParameter(name: String): Int {
        val value = getStringParameter(name)
        return try {
            value.toInt()
        } catch (e: NumberFormatException) {
            throw ParameterMisconfiguredException(name, "Integer parsing failed")
        }
    }

    override fun getDoubleParameter(name: String): Double {
        val value = getStringParameter(name)
        return try {
            value.toDouble()
        } catch (e: NumberFormatException) {
            throw ParameterMisconfiguredException(name, "Double parsing failed")
        }
    }
}