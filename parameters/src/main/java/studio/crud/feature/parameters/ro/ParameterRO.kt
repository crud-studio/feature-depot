package studio.crud.feature.parameters.ro

import studio.crud.crudframework.fieldmapper.annotation.DefaultMappingTarget
import studio.crud.crudframework.fieldmapper.annotation.MappedField
import studio.crud.feature.jpa.ro.AbstractJpaUpdatableRO
import studio.crud.feature.parameters.enums.ParameterType
import studio.crud.feature.parameters.model.Parameter

@DefaultMappingTarget(Parameter::class)
class ParameterRO : AbstractJpaUpdatableRO() {
    @MappedField
    var name: String? = null

    @MappedField
    var value: String? = null

    @MappedField
    var description: String? = null

    @MappedField
    var type: ParameterType? = null

    @MappedField
    var minValue: Int? = null

    @MappedField
    var maxValue: Int? = null
}