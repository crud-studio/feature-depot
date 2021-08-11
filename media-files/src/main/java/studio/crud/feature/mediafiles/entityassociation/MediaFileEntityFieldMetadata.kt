package studio.crud.feature.mediafiles.entityassociation

import studio.crud.feature.mediafiles.entityassociation.annotation.MediaFileField
import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaEntity
import java.lang.reflect.Field
import kotlin.reflect.KClass

class MediaFileEntityFieldMetadata(
    val entitySimpleName: String,
    val entityFullName: String,
    val entityClazz: KClass<out AbstractJpaEntity>,
    val fieldName: String,
    val field: Field,
    val annotation: MediaFileField
)