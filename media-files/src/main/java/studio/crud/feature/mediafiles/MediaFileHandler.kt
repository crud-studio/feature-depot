package studio.crud.feature.mediafiles

import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.model.MediaFile

interface MediaFileHandler {
    fun uploadFile(multipartFile: MultipartFile, alias: String?, description: String?, creatorObjectId: Long?, creatorObjectType: String?): MediaFile
    fun downloadFile(mediaFile: MediaFile): ByteArray
    fun getMediaFileByUuid(uuid: String): MediaFile
}