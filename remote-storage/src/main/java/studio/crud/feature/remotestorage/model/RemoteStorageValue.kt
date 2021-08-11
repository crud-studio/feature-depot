package studio.crud.feature.remotestorage.model

import com.antelopesystem.crudframework.crud.annotation.Deleteable
import com.antelopesystem.crudframework.fieldmapper.annotation.DefaultMappingTarget
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import studio.crud.feature.remotestorage.ro.RemoteStorageValueDTO
import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaUpdatableEntity
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

