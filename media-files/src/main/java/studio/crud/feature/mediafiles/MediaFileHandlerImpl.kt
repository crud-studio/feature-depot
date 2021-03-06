package studio.crud.feature.mediafiles

import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.crud.hooks.update.CRUDOnUpdateHook
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.modelfilter.dsl.where
import studio.crud.crudframework.utils.component.componentmap.annotation.ComponentMap
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FilenameUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.core.util.executeSingleOrNull
import studio.crud.feature.mediafiles.entityassociation.MediaFileEntityFieldMetadata
import studio.crud.feature.mediafiles.entityassociation.MediaFileEntityFieldResolver
import studio.crud.feature.mediafiles.entityassociation.exception.IncorrectFileAclModeException
import studio.crud.feature.mediafiles.entityassociation.exception.MediaFileEntityNotFoundException
import studio.crud.feature.mediafiles.entityassociation.exception.UnauthorizedFileExtensionException
import studio.crud.feature.mediafiles.enums.MediaFileAclMode
import studio.crud.feature.mediafiles.enums.MediaFileStorageType
import studio.crud.feature.mediafiles.exception.MediaFileLocationUnavailableException
import studio.crud.feature.mediafiles.exception.MediaFileNotFoundByIdException
import studio.crud.feature.mediafiles.exception.MediaFileNotFoundByUuidException
import studio.crud.feature.mediafiles.hooks.MediaFileAssociationHooks
import studio.crud.feature.mediafiles.model.MediaFile
import studio.crud.feature.mediafiles.storage.MediaFileStorageProvider
import java.util.*

@Component
class MediaFileHandlerImpl(
    private val primaryStorageProvider: MediaFileStorageProvider,
    private val mediaFileEntityFieldResolver: MediaFileEntityFieldResolver,
    private val crudHandler: CrudHandler,
    @Autowired(required = false)
    private val mediaFileAssociationHooks: List<MediaFileAssociationHooks> = emptyList()
) : MediaFileHandler {
    @ComponentMap
    lateinit var additionalStorageProviders: Map<MediaFileStorageType, List<MediaFileStorageProvider>>

    override fun uploadFile(file: MultipartFile, alias: String?, description: String?, creatorObjectId: Long?, creatorObjectType: String?, aclMode: MediaFileAclMode): MediaFile {
        val fileUuid = UUID.randomUUID().toString()
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
        val uploadedMediaFile = uploadFile(file, alias, description, creatorObjectId, creatorObjectType, metadata.annotationData.aclMode)
        associateMediaFile(uploadedMediaFile, entityId, entityName, fieldName)
        return uploadedMediaFile
    }

    override fun associateMediaFile(mediaFile: MediaFile, entityId: Long, entityName: String, fieldName: String) {
        val metadata = mediaFileEntityFieldResolver.getFieldMetadata(entityName, fieldName)
        validateAssociation(mediaFile, metadata)
        mediaFileAssociationHooks.forEach {
            if(it.entityClazz == metadata.entityClazz) {
                it.preAssociation(mediaFile, entityId, metadata)
            }
        }
        crudHandler.updateByFilter(where {
            "id" Equal entityId
        }, metadata.entityClazz.java as Class<BaseCrudEntity<Long>>)
            .withOnHook(CRUDOnUpdateHook {
                ReflectionUtils.makeAccessible(metadata.field)
                metadata.field.set(it, mediaFile)
            })
            // todo: enforceAccess
            .executeSingleOrNull() ?: throw MediaFileEntityNotFoundException(entityId, entityName)
    }

    override fun validateAssociation(mediaFile: MediaFile, metadata: MediaFileEntityFieldMetadata) {
        if(metadata.annotationData.allowedExtensions.isNotEmpty()) {
            val extension = mediaFile.extension ?: throw UnauthorizedFileExtensionException("none", metadata.annotationData.allowedExtensions.toSet())
            if(extension !in metadata.annotationData.allowedExtensions) {
                throw UnauthorizedFileExtensionException(extension, metadata.annotationData.allowedExtensions.toSet())
            }
        }

        if(mediaFile.aclMode != metadata.annotationData.aclMode) {
            throw IncorrectFileAclModeException(mediaFile.aclMode, metadata.annotationData.aclMode)
        }
    }

    override fun deleteAssociatedMediaFile(entityId: Long, entityName: String, fieldName: String) {
        val metadata = mediaFileEntityFieldResolver.getFieldMetadata(entityName, fieldName)
        mediaFileAssociationHooks.forEach {
            if(it.entityClazz == metadata.entityClazz) {
                it.preDeleteAssociation(entityId, metadata)
            }
        }
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

    override fun getMediaFileById(id: Long): MediaFile {
        return crudHandler.show(id, MediaFile::class.java)
            .execute() ?: throw MediaFileNotFoundByIdException(id)
    }
}