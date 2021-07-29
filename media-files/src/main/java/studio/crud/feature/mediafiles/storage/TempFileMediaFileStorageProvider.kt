package studio.crud.feature.mediafiles.storage

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.enums.MediaFileStorageType
import studio.crud.feature.mediafiles.exception.MediaFileDownloadFailedException
import studio.crud.feature.mediafiles.exception.MediaFileUploadFailedException
import studio.crud.feature.mediafiles.model.RemoteFileMetadataPojo
import java.io.File
import java.nio.file.Paths

/**
 * Fallback Temp File Media File Storage implementation
 */
@Component
class TempFileMediaFileStorageProvider(
    private val tempDirectory: String = System.getProperty("java.io.tmpdir")
) : MediaFileStorageProvider {
    override val type: MediaFileStorageType = MediaFileStorageType.TempFile
    override val location: String = tempDirectory

    override fun upload(filename: String, file: MultipartFile): RemoteFileMetadataPojo {
        try {
            val tempFile = File.createTempFile(filename, "-temp")
            file.transferTo(tempFile)
            return RemoteFileMetadataPojo(
                tempFile.name,
                tempFile.totalSpace,
                location
            )
        } catch (e: Exception) {
            log.error(e) { "Temp file upload failed" }
            throw MediaFileUploadFailedException(e.message).initCause(e)
        }
    }

    override fun download(filename: String): ByteArray {
        try {
            val file = File(Paths.get(tempDirectory, filename).toString())
            return file.readBytes()
        } catch (e: Exception) {
            log.error(e) { "Temp file download failed" }
            throw MediaFileDownloadFailedException(e.message).initCause(e)
        }
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}