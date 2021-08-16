package studio.crud.feature.mediafiles.entityassociation

import studio.crud.feature.mediafiles.entityassociation.annotation.MediaFileField
import studio.crud.feature.mediafiles.enums.MediaFileAclMode
import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaEntity
import java.lang.reflect.Field
import kotlin.reflect.KClass

class MediaFileEntityFieldMetadata(
    val entitySimpleName: String,
    val entityFullName: String,
    val entityClazz: KClass<out AbstractJpaEntity>,
    val fieldName: String,
    val field: Field,
    val annotationData: MediaFileFieldData
)

data class MediaFileFieldData(
    val allowedExtensions: Array<String> = emptyArray(),
    val aclMode: MediaFileAclMode = MediaFileAclMode.PRIVATE
) {
    constructor(annotation: MediaFileField) : this(annotation.allowedExtensions, annotation.aclMode)
}