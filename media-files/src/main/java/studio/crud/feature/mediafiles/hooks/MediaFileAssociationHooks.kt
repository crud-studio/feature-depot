package studio.crud.feature.mediafiles.hooks

import studio.crud.feature.mediafiles.entityassociation.MediaFileEntityFieldMetadata
import studio.crud.feature.mediafiles.model.MediaFile
import kotlin.reflect.KClass

interface MediaFileAssociationHooks {
    /**
     * The entity class which will be associated
     */
    val entityClazz: KClass<out Any>

    /**
     * Ran before media file association
     */
    fun preAssociation(mediaFile: MediaFile, entityId: Long, metadata: MediaFileEntityFieldMetadata) {}

    /**
     * Ran before a media file association is deleted
     */
    fun preDeleteAssociation(entityId: Long, metadata: MediaFileEntityFieldMetadata) {}
}