package studio.crud.feature.mediafiles

import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.enums.MediaFileAclMode
import studio.crud.feature.mediafiles.model.MediaFile

interface MediaFileHandler {
    fun uploadFile(file: MultipartFile, alias: String?, description: String?, creatorObjectId: Long?, creatorObjectType: String?, aclMode: MediaFileAclMode): MediaFile
    fun uploadAndAssociateFile(file: MultipartFile, alias: String?, description: String?, entityId: Long, entityName: String, fieldName: String, creatorObjectId: Long?, creatorObjectType: String?): MediaFile
    fun downloadFile(mediaFile: MediaFile): ByteArray
    fun getMediaFileByUuid(uuid: String): MediaFile
}