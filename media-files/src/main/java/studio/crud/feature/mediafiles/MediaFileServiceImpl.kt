package studio.crud.feature.mediafiles

import studio.crud.crudframework.crud.handler.CrudHandler
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.core.audit.util.CurrentEntityResolver
import studio.crud.feature.mediafiles.enums.MediaFileAclMode
import studio.crud.feature.mediafiles.exception.MediaFileAccessDeniedException
import studio.crud.feature.mediafiles.exception.MediaFileNotFoundByUuidException
import studio.crud.feature.mediafiles.hooks.MediaFileDownloadHooks
import studio.crud.feature.mediafiles.postprocessor.MediaFilePostProcessor
import studio.crud.feature.mediafiles.ro.MediaFileRO
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.jvm.jvmName

@Service
class MediaFileServiceImpl(
    private val crudHandler: CrudHandler,
    private val mediaFileHandler: MediaFileHandler,
    @Autowired(required = false)
    private val currentEntityResolver: CurrentEntityResolver?,
    @Autowired(required = false)
    private val mediaFileDownloadHooks: List<MediaFileDownloadHooks> = emptyList(),
    @Autowired(required = false)
    private val mediaFilePostProcessors: List<MediaFilePostProcessor> = emptyList()
) : MediaFileService {
    override fun uploadFile(file: MultipartFile, alias: String?, description: String?, aclMode: MediaFileAclMode): MediaFileRO {
        val resolvedCreator = currentEntityResolver?.resolve()
        val mediaFile = mediaFileHandler.uploadFile(file, alias, description, resolvedCreator?.objectId?.toLong(), resolvedCreator?.objectType, aclMode)
        return crudHandler.fill(mediaFile, MediaFileRO::class.java)
    }

    override fun deleteAssociatedMediaFile(entityId: Long, entityName: String, fieldName: String) {
        mediaFileHandler.deleteAssociatedMediaFile(entityId, entityName, fieldName)
    }

    override fun uploadAndAssociateFile(file: MultipartFile, alias: String?, description: String?, entityId: Long, entityName: String, fieldName: String): MediaFileRO {
        val resolvedCreator = currentEntityResolver?.resolve()
        val mediaFile = mediaFileHandler.uploadAndAssociateFile(file, alias, description, entityId, entityName, fieldName, resolvedCreator?.objectId?.toLong(), resolvedCreator?.objectType)
        return crudHandler.fill(mediaFile, MediaFileRO::class.java)
    }

    override fun downloadFile(uuid: String, parameters: Map<String, List<String>>, response: HttpServletResponse) {
        try {
            val mediaFile =  mediaFileHandler.getMediaFileByUuid(uuid)
            try {
                mediaFileDownloadHooks.forEach { it.preDownload(mediaFile) }
            } catch (e: MediaFileAccessDeniedException) {
                response.status = HttpStatus.NOT_FOUND.value()
                return
            }
            var bytes = mediaFileHandler.downloadFile(mediaFile)
            mediaFileDownloadHooks.forEach { it.postDownload(mediaFile, bytes) }
            mediaFilePostProcessors
                .filter { postProcessor -> mediaFile.extension in postProcessor.supportedExtensions() }
                .filter { postProcessor -> postProcessor.supportedParameters().all { parameters.containsKey(it) }}
                .forEach { postProcessor ->
                    try {
                        bytes = postProcessor.run(mediaFile, bytes, parameters)
                    } catch(e: Exception) {
                        log.error(e) { "Post processor [ ${postProcessor::class.jvmName} ] failed"}
                    }
                }
            response.contentType = getContentTypeFromExtension(mediaFile.extension!!)
            val fileName = mediaFile.alias ?: mediaFile.name
            response.setHeader("Content-disposition", "attachment; filename=$fileName")
            response.outputStream.write(bytes)
        } catch(e: MediaFileNotFoundByUuidException) {
            response.status = HttpStatus.NOT_FOUND.value()
        }
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

    companion object {
        private val log = KotlinLogging.logger { }
    }
}