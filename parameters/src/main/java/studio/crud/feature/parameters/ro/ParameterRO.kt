package studio.crud.feature.parameters.ro

import com.antelopesystem.crudframework.fieldmapper.annotation.DefaultMappingTarget
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import studio.crud.feature.parameters.enums.ParameterType
import studio.crud.feature.parameters.model.Parameter
import studio.crud.sharedcommon.crud.jpa.ro.AbstractJpaUpdatableRO

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