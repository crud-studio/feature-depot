package studio.crud.feature.mediafiles.entityassociation

import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.feature.mediafiles.entityassociation.annotation.MediaFileField
import studio.crud.feature.mediafiles.enums.MediaFileAclMode
import java.lang.reflect.Field
import kotlin.reflect.KClass

class MediaFileEntityFieldMetadata(
    val entitySimpleName: String,
    val entityFullName: String,
    val entityClazz: KClass<out BaseCrudEntity<*>>,
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