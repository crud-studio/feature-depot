package studio.crud.feature.mediafiles.model

import com.antelopesystem.crudframework.fieldmapper.annotation.DefaultMappingTarget
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedFields
import studio.crud.feature.jpa.model.AbstractJpaUpdatableEntity
import studio.crud.feature.mediafiles.enums.MediaFileAclMode
import studio.crud.feature.mediafiles.enums.MediaFileStorageType
import studio.crud.feature.mediafiles.ro.MediaFileRO
import studio.crud.feature.mediafiles.ro.MinimalMediaFileRO
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "media_file")
@DefaultMappingTarget(MediaFileRO::class)
class MediaFile(
    @Column
    @Enumerated(EnumType.STRING)
    var storageType: MediaFileStorageType,

    /**
     * Integration-specific location, used by the integration to locate the file
     */
    @Column(name = "location", nullable = false)
    var location: String,

    /**
     * The original file name
     */
    @MappedFields(
        MappedField(),
        MappedField(target = MinimalMediaFileRO::class)
    )
    @Column(name = "name", nullable = false)
    var name: String,

    /**
     * The actual remote file name
     */
    @Column(name = "remote_name", nullable = false)
    var remoteName: String
) : AbstractJpaUpdatableEntity() {
    /**
     * Internal UUID
     */
    @MappedFields(
        MappedField(),
        MappedField(target = MinimalMediaFileRO::class)
    )
    @Column(name = "uuid", nullable = false, updatable = false)
    var uuid: String = UUID.randomUUID().toString()


    /**
     * The file hash
     */
    @Column(name = "file_hash", nullable = true)
    var fileHash: String? = null


    /**
     * The object ID who created/uploaded the file
     */
    @MappedField
    @Column(nullable = true)
    var creatorObjectId: Long? = null

    /**
     * The object type created/uploaded the file
     */
    @MappedField
    @Column(nullable = true)
    var creatorObjectType: String? = null

    /**
     * The file's extension
     */
    @MappedFields(
        MappedField(),
        MappedField(target = MinimalMediaFileRO::class)
    )
    @Column(name = "extension", nullable = true)
    var extension: String? = null

    /**
     * The file's size in bytes
     */
    @MappedFields(
        MappedField(),
        MappedField(target = MinimalMediaFileRO::class)
    )
    @Column(name = "size", nullable = true)
    var size: Long? = null

    /**
     * Optional description for the file
     */
    @MappedField
    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    var description: String? = null

    /**
     * Optional file name alias
     */
    @MappedFields(
        MappedField(),
        MappedField(target = MinimalMediaFileRO::class)
    )
    @Column(name = "alias", nullable = true)
    var alias: String? = null

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PRIVATE'")
    @Enumerated(EnumType.STRING)
    var aclMode: MediaFileAclMode = MediaFileAclMode.PRIVATE
}