package studio.crud.feature.mediafiles.ro

import com.antelopesystem.crudframework.fieldmapper.annotation.DefaultMappingTarget
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import studio.crud.feature.mediafiles.model.MediaFile
import studio.crud.sharedcommon.crud.jpa.ro.AbstractJpaUpdatableRO

@DefaultMappingTarget(MediaFile::class)
open class MediaFileRO: AbstractJpaUpdatableRO() {
    var uuid: String? = null

    var name: String? = null

    var extension: String? = null

    var size: Long? = null

    @MappedField
    var alias: String? = null

    @MappedField
    var description: String? = null

    var creatorObjectId: Long? = null

    var creatorObjectType: String? = null

    var fileHash: String? = null
}