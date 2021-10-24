package studio.crud.feature.parameters.crud

import com.antelopesystem.crudframework.crud.hooks.interfaces.CreateFromHooks
import com.antelopesystem.crudframework.crud.hooks.interfaces.CreateHooks
import com.antelopesystem.crudframework.crud.hooks.interfaces.UpdateFromHooks
import com.antelopesystem.crudframework.crud.hooks.interfaces.UpdateHooks
import org.springframework.stereotype.Component
import studio.crud.feature.parameters.enums.ParameterType
import studio.crud.feature.parameters.exception.InvalidParameterTypeException
import studio.crud.feature.parameters.exception.InvalidParameterValueException
import studio.crud.feature.parameters.model.Parameter

@Component
class ParameterPersistentHooks: CreateHooks<Long, Parameter>, CreateFromHooks<Long, Parameter>, UpdateHooks<Long, Parameter>, UpdateFromHooks<Long, Parameter> {
    override fun onCreate(entity: Parameter) {
        verifyParameterValue(entity)
    }

    override fun onCreateFrom(entity: Parameter, ro: Any) {
        verifyParameterValue(entity)
    }

    override fun onUpdate(entity: Parameter) {
        verifyParameterValue(entity)
    }

    override fun onUpdateFrom(entity: Parameter, ro: Any) {
        verifyParameterValue(entity)
    }

    private fun verifyParameterValue(parameter: Parameter) {
        when (parameter.type) {
            ParameterType.String -> {
            }
            ParameterType.Boolean -> if (!parameter.value.equals("true", ignoreCase = true) && !parameter.value.equals("false", ignoreCase = true)) {
                throw InvalidParameterValueException(parameter.value)
            }
            ParameterType.Integer -> try {
                val value = parameter.value.toInt()
                if ((parameter.maxValue != null && value > parameter.maxValue!!) || (parameter.minValue != null && value < parameter.minValue!!)) {
                    throw InvalidParameterValueException(parameter.value)
                }
            } catch (e: NumberFormatException) {
                throw InvalidParameterValueException(parameter.value)
            }
            ParameterType.Double -> try {
                val value = parameter.value.toDouble()
                if ((parameter.maxValue != null && value > parameter.maxValue!!) || (parameter.minValue != null && value < parameter.minValue!!)) {
                    throw InvalidParameterValueException(parameter.value)
                }
            } catch (e: NumberFormatException) {
                throw InvalidParameterValueException(parameter.value)
            }
            else -> throw InvalidParameterTypeException()
        }
    }
}