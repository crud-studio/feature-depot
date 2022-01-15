package studio.crud.feature.mediafiles.storage

import studio.crud.crudframework.utils.component.componentmap.annotation.ComponentMapKey
import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.exception.MediaFileDownloadFailedException
import studio.crud.feature.mediafiles.exception.MediaFileUploadFailedException
import studio.crud.feature.mediafiles.enums.MediaFileStorageType
import studio.crud.feature.mediafiles.model.RemoteFileMetadataPojo

/**
 * Represents a facade for uploading/downloading files
 */
interface MediaFileStorageProvider {

    /**
     * The storage type
     */
    @get:ComponentMapKey
    val type: MediaFileStorageType

    /**
     * The location of this storage, will be marked on the media file
     */
    val location: String

    /**
     * Upload a file to storage
     * @throws MediaFileUploadFailedException
     */
    fun upload(filename: String, file: MultipartFile): RemoteFileMetadataPojo

    /**
     * Download a file from storage
     * @throws MediaFileDownloadFailedException
     */
    fun download(filename: String): ByteArray
}