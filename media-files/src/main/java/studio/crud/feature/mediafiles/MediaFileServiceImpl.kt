package studio.crud.feature.mediafiles

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.exception.MediaFileNotFoundByUuidException
import studio.crud.feature.mediafiles.model.MediaFile
import studio.crud.feature.mediafiles.ro.MediaFileRO
import javax.servlet.http.HttpServletResponse

@Service
class MediaFileServiceImpl(
    private val crudHandler: CrudHandler,
    private val mediaFileHandler: MediaFileHandler
) : MediaFileService {


    override fun uploadFile(multipartFile: MultipartFile, alias: String?, description: String?): MediaFileRO {
        val mediaFile = mediaFileHandler.uploadFile(multipartFile, alias, description)
        return crudHandler.getRO(mediaFile, MediaFileRO::class.java)
    }

    override fun downloadFile(uuid: String, response: HttpServletResponse) {
        try {
            val (mediaFile, bytes) = mediaFileHandler.downloadFile(uuid)
            val hasAccess = true // todo
            if(!hasAccess) {
                response.status = HttpStatus.NOT_FOUND.value()
                return
            }
            response.contentType = getContentTypeFromExtension(mediaFile.extension!!)
            val fileName = mediaFile.alias ?: mediaFile.name
            response.setHeader("Content-disposition", "attachment; filename=$fileName")
            response.outputStream.write(bytes)
        } catch(e: MediaFileNotFoundByUuidException) {
            response.status = HttpStatus.NOT_FOUND.value()
        }
    }

    private fun hasAccessToMediaFile(mediaFile: MediaFile, currentUserId: Long?): Boolean {
        return true
//        return when(mediaFile.aclMode) {
//            MediaFileAclMode.PRIVATE -> mediaFile.objectId == currentUserId
//            MediaFileAclMode.LOGGED_IN -> currentUserId != null
//            MediaFileAclMode.PUBLIC -> true
//        }
    }

    private fun getContentTypeFromExtension(extension: String): String? {
        if (extension.equals("png", ignoreCase = true) || extension.equals("gif", ignoreCase = true) || extension.equals("bmp", ignoreCase = true) || extension.equals(
                "jpeg",
                ignoreCase = true
            ) || extension.equals("tiff", ignoreCase = true)
        ) {
            return "image/$extension"
        } else if (extension.equals("jpg", ignoreCase = true)) {
            return "image/jpeg"
        } else if (extension.equals("tif", ignoreCase = true)) {
            return "image/tiff"
        } else if (extension.equals("pdf", ignoreCase = true)) {
            return "application/pdf"
        } else {
            return "application/octet-stream"
        }
    }
}