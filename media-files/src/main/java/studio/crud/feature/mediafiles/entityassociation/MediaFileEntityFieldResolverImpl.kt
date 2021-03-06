package studio.crud.feature.mediafiles.entityassociation

import studio.crud.crudframework.model.BaseCrudEntity
import org.reflections.Reflections
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import studio.crud.feature.mediafiles.config.MediaFileFeatureProperties
import studio.crud.feature.mediafiles.entityassociation.annotation.MediaFileField
import studio.crud.feature.mediafiles.entityassociation.exception.MediaFileFieldNotFoundException
import studio.crud.feature.mediafiles.entityassociation.exception.MediaFileFieldTypeNotMediaFileException
import studio.crud.feature.mediafiles.model.MediaFile

@Component
class MediaFileEntityFieldResolverImpl(
    val properties: MediaFileFeatureProperties
) : MediaFileEntityFieldResolver, InitializingBean {
    private lateinit var mediaFileEntityFieldMetadatas: List<MediaFileEntityFieldMetadata>

    private val mediaFileEntityFieldMetadataCache: MutableMap<Pair<String, String>, MediaFileEntityFieldMetadata?> = mutableMapOf()

    override fun afterPropertiesSet() {
        val reflections = Reflections(properties.basePackage)
        mediaFileEntityFieldMetadatas = reflections.getSubTypesOf(BaseCrudEntity::class.java).flatMap { clazz ->
            clazz.declaredFields
                .filter { field -> field.isAnnotationPresent(MediaFileField::class.java) }
                .map { field ->
                    if(field.type != MediaFile::class.java) {
                        throw MediaFileFieldTypeNotMediaFileException(clazz.name, field.name)
                    }
                    MediaFileEntityFieldMetadata(
                        clazz.simpleName,
                        clazz.name,
                        clazz.kotlin,
                        field.name,
                        field,
                        MediaFileFieldData(field.getDeclaredAnnotation(MediaFileField::class.java))
                    )
                }
        }
    }

    override fun getFieldMetadata(entityName: String, fieldName: String): MediaFileEntityFieldMetadata {
        val key = entityName to fieldName
        return mediaFileEntityFieldMetadataCache.computeIfAbsent(key) {
            mediaFileEntityFieldMetadatas.find { it.entitySimpleName == entityName && it.fieldName == fieldName }
        } ?: throw MediaFileFieldNotFoundException(entityName, fieldName)
    }
}

