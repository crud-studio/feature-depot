package studio.crud.feature.mediafiles

import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.model.MediaFile

interface MediaFileHandler {
    fun uploadFile(multipartFile: MultipartFile, alias: String?, description: String?): MediaFile
    fun downloadFile(uuid: String): Pair<MediaFile, ByteArray>
    fun getMediaFileByUuid(uuid: String): MediaFile
}