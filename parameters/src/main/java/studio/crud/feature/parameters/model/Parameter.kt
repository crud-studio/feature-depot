package studio.crud.feature.parameters.model

import com.antelopesystem.crudframework.crud.annotation.CachedBy
import com.antelopesystem.crudframework.crud.annotation.Deleteable
import com.antelopesystem.crudframework.fieldmapper.annotation.DefaultMappingTarget
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import studio.crud.feature.parameters.enums.ParameterType
import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaUpdatableEntity
import studio.crud.feature.parameters.ro.ParameterRO
import javax.persistence.*

@Entity
@Table
@DefaultMappingTarget(ParameterRO::class)
@CachedBy("parameterCache")
@Deleteable(softDelete = false)
class Parameter(
    @MappedField
    @Column(nullable = false, updatable = false)
    var name: String,

    @MappedField
    @Column(nullable = false)
    var value: String,

    @MappedField
    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    var type: ParameterType
) : AbstractJpaUpdatableEntity() {
    @Column(nullable = true)
    @MappedField
    var description: String? = null

    @Column(updatable = false)
    @MappedField
    var minValue: Int? = null

    @Column(updatable = false)
    @MappedField
    var maxValue: Int? = null
}