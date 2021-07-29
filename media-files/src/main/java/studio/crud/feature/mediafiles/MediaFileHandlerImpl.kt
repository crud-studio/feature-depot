package studio.crud.feature.mediafiles

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.modelfilter.dsl.where
import com.antelopesystem.crudframework.utils.component.componentmap.annotation.ComponentMap
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FilenameUtils
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.enums.MediaFileStorageType
import studio.crud.feature.mediafiles.exception.MediaFileLocationUnavailableException
import studio.crud.feature.mediafiles.exception.MediaFileNotFoundByUuidException
import studio.crud.feature.mediafiles.model.MediaFile
import studio.crud.feature.mediafiles.storage.MediaFileStorageProvider
import studio.crud.sharedcommon.extentions.toDoubleBase64ForUrls
import java.util.*

@Component
class MediaFileHandlerImpl(
    private val primaryStorageProvider: MediaFileStorageProvider,
    private val crudHandler: CrudHandler
) : MediaFileHandler {
    @ComponentMap
    private lateinit var additionalStorageProviders: Map<MediaFileStorageType, List<MediaFileStorageProvider>>

    override fun uploadFile(multipartFile: MultipartFile, alias: String?, description: String?): MediaFile {
        val fileUuid = UUID.randomUUID().toDoubleBase64ForUrls()
        val fileHash = DigestUtils.sha256Hex(multipartFile.bytes)
        val extension = FilenameUtils.getExtension(multipartFile.originalFilename).toLowerCase()
        val fileName = multipartFile.originalFilename
        val remoteFileName = "${fileUuid}_$fileName"
        val result = primaryStorageProvider.upload(remoteFileName, multipartFile)
        val mediaFile = MediaFile(primaryStorageProvider.type, result.location, fileName, result.name)
        mediaFile.extension = extension
        mediaFile.size = result.size
        mediaFile.fileHash = fileHash
        mediaFile.alias = alias
        mediaFile.description = description
        return crudHandler.create(mediaFile).execute()
    }

    override fun downloadFile(uuid: String): Pair<MediaFile, ByteArray> {
        val mediaFile = getMediaFileByUuid(uuid)
        val storageProvider = additionalStorageProviders[mediaFile.storageType]?.find { it.location == mediaFile.location } ?: throw MediaFileLocationUnavailableException()
        return mediaFile to storageProvider.download(mediaFile.remoteName)
    }

    override fun getMediaFileByUuid(uuid: String): MediaFile {
        return crudHandler.showBy(where {
            "uuid" Equal uuid
        }, MediaFile::class.java)
            .execute() ?: throw MediaFileNotFoundByUuidException(uuid)
    }
}