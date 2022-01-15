package studio.crud.feature.mediafiles.crud

import studio.crud.crudframework.fieldmapper.transformer.base.FieldTransformerBase
import studio.crud.crudframework.utils.utils.ReflectionUtils
import org.springframework.stereotype.Component
import studio.crud.feature.mediafiles.MediaFileHandler
import studio.crud.feature.mediafiles.entityassociation.MediaFileEntityFieldResolver
import studio.crud.feature.mediafiles.model.MediaFile
import studio.crud.feature.mediafiles.ro.MinimalMediaFileRO
import java.lang.reflect.Field

@Component
class MinimalMediaFileROAssociationTransformer(
    private val mediaFileHandler: MediaFileHandler,
    private val mediaFileEntityFieldResolver: MediaFileEntityFieldResolver
) : FieldTransformerBase<MinimalMediaFileRO, MediaFile>() {
    override fun isDefault(): Boolean = true

    override fun innerTransform(fromField: Field, toField: Field, originalValue: MinimalMediaFileRO?, fromObject: Any, toObject: Any): MediaFile? {
        originalValue ?: return null
        val mediaFileId = originalValue.id
        if(mediaFileId == null) { // If {} is sent, will delete the media file from the entity, temporary approach: todo
            ReflectionUtils.makeAccessible(toField)
            toField.set(toObject, null)
            return null
        }
        mediaFileHandler.getMediaFileById(mediaFileId)
        val mediaFile = mediaFileHandler.getMediaFileById(mediaFileId)
        mediaFileHandler.validateAssociation(mediaFile, mediaFileEntityFieldResolver.getFieldMetadata(toObject::class.java.simpleName, fromField.name))
        return mediaFile
    }
}