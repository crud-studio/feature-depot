package studio.crud.feature.auth.entity.model

import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import studio.crud.feature.auth.authentication.mfa.enums.MfaType
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaUpdatableEntity
import javax.persistence.*
import javax.persistence.Entity as JpaEntity

@JpaEntity
@Table(name = "auth_entity_mfa_method")
class EntityMfaMethod(
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entity_id")
    var entity: Entity,

    @Column
    @Enumerated(EnumType.STRING)
    var type: MfaType,

    @MappedField(target = CustomParamsDTO::class)
    var param1: String? = null,

    @MappedField(target = CustomParamsDTO::class)
    var param2: String? = null,

    @MappedField(target = CustomParamsDTO::class)
    var param3: String? = null,

    @MappedField(target = CustomParamsDTO::class)
    var param4: String? = null,

    @MappedField(target = CustomParamsDTO::class)
    var param5: String? = null
) : AbstractJpaUpdatableEntity() {
    constructor(entity: Entity, type: MfaType, params: CustomParamsDTO) : this(entity, type, params.param1, params.param2, params.param3, params.param4, params.param5)
}