package studio.crud.feature.mediafiles

import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.entityassociation.MediaFileEntityFieldMetadata
import studio.crud.feature.mediafiles.enums.MediaFileAclMode
import studio.crud.feature.mediafiles.model.MediaFile

interface MediaFileHandler {
    fun uploadFile(file: MultipartFile, alias: String?, description: String?, creatorObjectId: Long?, creatorObjectType: String?, aclMode: MediaFileAclMode): MediaFile
    fun uploadAndAssociateFile(file: MultipartFile, alias: String?, description: String?, entityId: Long, entityName: String, fieldName: String, creatorObjectId: Long?, creatorObjectType: String?): MediaFile
    fun associateMediaFile(mediaFile: MediaFile, entityId: Long, entityName: String, fieldName: String)
    fun downloadFile(mediaFile: MediaFile): ByteArray
    fun getMediaFileByUuid(uuid: String): MediaFile
    fun getMediaFileById(id: Long): MediaFile
    fun deleteAssociatedMediaFile(entityId: Long, entityName: String, fieldName: String)
    fun validateAssociation(mediaFile: MediaFile, metadata: MediaFileEntityFieldMetadata)
}