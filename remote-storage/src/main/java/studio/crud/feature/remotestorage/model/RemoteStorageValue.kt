package studio.crud.feature.remotestorage.model

import studio.crud.crudframework.crud.annotation.Deleteable
import studio.crud.crudframework.fieldmapper.annotation.DefaultMappingTarget
import studio.crud.crudframework.fieldmapper.annotation.MappedField
import studio.crud.feature.jpa.model.AbstractJpaUpdatableEntity
import studio.crud.feature.remotestorage.ro.RemoteStorageValueDTO
import javax.persistence.Column
import javax.persistence.Entity

@Entity
@DefaultMappingTarget(RemoteStorageValueDTO::class)
@Deleteable(false)
class RemoteStorageValue(
    @MappedField
    @Column(length = 1024, updatable = false)
    var identifier: String,

    @MappedField
    @Column(columnDefinition = "LONGTEXT")
    var value: String,

    @Column
    var ownerId: String,

    @Column
    var ownerType: String
) : AbstractJpaUpdatableEntity()

