package studio.crud.feature.mediafiles

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.crud.hooks.update.CRUDOnUpdateHook
import com.antelopesystem.crudframework.model.BaseCrudEntity
import com.antelopesystem.crudframework.modelfilter.dsl.where
import com.antelopesystem.crudframework.utils.component.componentmap.annotation.ComponentMap
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FilenameUtils
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.entityassociation.MediaFileEntityFieldResolver
import studio.crud.feature.mediafiles.entityassociation.exception.MediaFileEntityNotFoundException
import studio.crud.feature.mediafiles.entityassociation.exception.UnauthorizedFileExtensionException
import studio.crud.feature.mediafiles.enums.MediaFileAclMode
import studio.crud.feature.mediafiles.enums.MediaFileStorageType
import studio.crud.feature.mediafiles.exception.MediaFileLocationUnavailableException
import studio.crud.feature.mediafiles.exception.MediaFileNotFoundByUuidException
import studio.crud.feature.mediafiles.model.MediaFile
import studio.crud.feature.mediafiles.storage.MediaFileStorageProvider
import studio.crud.sharedcommon.crud.executeSingleOrNull
import studio.crud.sharedcommon.extentions.toDoubleBase64ForUrls
import java.util.*

@Component
class MediaFileHandlerImpl(
    private val primaryStorageProvider: MediaFileStorageProvider,
    private val mediaFileEntityFieldResolver: MediaFileEntityFieldResolver,
    private val crudHandler: CrudHandler
) : MediaFileHandler {
    @ComponentMap
    private lateinit var additionalStorageProviders: Map<MediaFileStorageType, List<MediaFileStorageProvider>>

    override fun uploadFile(file: MultipartFile, alias: String?, description: String?, creatorObjectId: Long?, creatorObjectType: String?, aclMode: MediaFileAclMode): MediaFile {
        val fileUuid = UUID.randomUUID().toDoubleBase64ForUrls()
        val fileHash = DigestUtils.sha256Hex(file.bytes)
        val extension = FilenameUtils.getExtension(file.originalFilename).toLowerCase()
        val fileName = file.originalFilename
        val remoteFileName = "${fileUuid}_$fileName"
        val result = primaryStorageProvider.upload(remoteFileName, file)
        val mediaFile = MediaFile(primaryStorageProvider.type, result.location, fileName, result.name)
        mediaFile.extension = extension
        mediaFile.size = result.size
        mediaFile.fileHash = fileHash
        mediaFile.alias = alias
        mediaFile.description = description
        mediaFile.creatorObjectId = creatorObjectId
        mediaFile.creatorObjectType = creatorObjectType
        mediaFile.aclMode = aclMode
        return crudHandler.create(mediaFile).execute()
    }

    override fun uploadAndAssociateFile(file: MultipartFile, alias: String?, description: String?, entityId: Long, entityName: String, fieldName: String, creatorObjectId: Long?, creatorObjectType: String?): MediaFile {
        val metadata = mediaFileEntityFieldResolver.getFieldMetadata(entityName, fieldName)
        val extension = FilenameUtils.getExtension(file.originalFilename)
        if(metadata.annotation.allowedExtensions.isNotEmpty() && extension !in metadata.annotation.allowedExtensions) {
            throw UnauthorizedFileExtensionException(extension, metadata.annotation.allowedExtensions.toSet())
        }

        val uploadedMediaFile = uploadFile(file, alias, description, creatorObjectId, creatorObjectType, metadata.annotation.aclMode)
        crudHandler.updateByFilter(where {
            "id" Equal entityId
        }, metadata.entityClazz.java as Class<BaseCrudEntity<Long>>)
            .withOnHook(CRUDOnUpdateHook {
                ReflectionUtils.makeAccessible(metadata.field)
                metadata.field.set(it, uploadedMediaFile)
            })
                // todo: enforceAccess
            .executeSingleOrNull() ?: throw MediaFileEntityNotFoundException(entityId, entityName)
        return uploadedMediaFile
    }

    override fun deleteAssociatedMediaFile(entityId: Long, entityName: String, fieldName: String) {
        val metadata = mediaFileEntityFieldResolver.getFieldMetadata(entityName, fieldName)
        crudHandler.updateByFilter(where {
            "id" Equal entityId
        }, metadata.entityClazz.java as Class<BaseCrudEntity<Long>>)
            .withOnHook(CRUDOnUpdateHook {
                ReflectionUtils.makeAccessible(metadata.field)
                metadata.field.set(it, null)
            })
            // todo: enforceAccess
            .executeSingleOrNull() ?: throw MediaFileEntityNotFoundException(entityId, entityName)
    }

    override fun downloadFile(mediaFile: MediaFile): ByteArray {
        val storageProvider = additionalStorageProviders[mediaFile.storageType]?.find { it.location == mediaFile.location } ?: throw MediaFileLocationUnavailableException()
        return storageProvider.download(mediaFile.remoteName)
    }

    override fun getMediaFileByUuid(uuid: String): MediaFile {
        return crudHandler.showBy(where {
            "uuid" Equal uuid
        }, MediaFile::class.java)
            .execute() ?: throw MediaFileNotFoundByUuidException(uuid)
    }
}