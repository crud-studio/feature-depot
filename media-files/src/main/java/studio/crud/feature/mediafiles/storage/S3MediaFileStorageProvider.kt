package studio.crud.feature.mediafiles.storage

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.config.MediaFileS3Properties
import studio.crud.feature.mediafiles.enums.MediaFileStorageType
import studio.crud.feature.mediafiles.exception.MediaFileDownloadFailedException
import studio.crud.feature.mediafiles.exception.MediaFileUploadFailedException
import studio.crud.feature.mediafiles.model.RemoteFileMetadataPojo
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

/**
 * S3 Media File Storage implementation
 */
@Primary
@Component
@ConditionalOnProperty(prefix = MediaFileS3Properties.PREFIX, name = ["enabled"], havingValue = "true")
class S3MediaFileStorageProvider(
    private val mediaFileS3Properties: MediaFileS3Properties,
    private val s3Client: AmazonS3
) : MediaFileStorageProvider, CoroutineScope {
    override val type: MediaFileStorageType = MediaFileStorageType.S3
    override val location: String = "s3:${mediaFileS3Properties.bucket}"

    override val coroutineContext: CoroutineContext
        get() = Executors.newCachedThreadPool().asCoroutineDispatcher()

    override fun upload(filename: String, file: MultipartFile): RemoteFileMetadataPojo {
        try {
            val s3Uuid = UUID.randomUUID().toString()
            s3Client.putObject(
                PutObjectRequest(mediaFileS3Properties.bucket,
                    s3Uuid,
                    file.inputStream,
                    ObjectMetadata().apply {
                        contentLength = file.size
                        contentType = file.contentType
                    }
                )
            )
            val metadata = s3Client.getObjectMetadata(mediaFileS3Properties.bucket, s3Uuid)
            return RemoteFileMetadataPojo(
                s3Uuid,
                metadata.contentLength,
                location
            )
        } catch (e: Exception) {
            log.error(e) { "S3 file upload failed" }
            throw MediaFileUploadFailedException(e.message).initCause(e)
        }

    }

    override fun download(filename: String): ByteArray {
        try {
            val downloaded = s3Client.getObject(mediaFileS3Properties.bucket, filename)
            return downloaded.objectContent.readBytes()
        } catch (e: Exception) {
            log.error(e) { "S3 file download failed" }
            throw MediaFileDownloadFailedException(e.message).initCause(e)
        }
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}