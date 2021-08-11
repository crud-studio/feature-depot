package studio.crud.feature.mediafiles.entityassociation

import org.reflections.Reflections
import org.springframework.stereotype.Component
import studio.crud.feature.mediafiles.entityassociation.annotation.MediaFileField
import studio.crud.feature.mediafiles.entityassociation.exception.MediaFileFieldNotFoundException
import studio.crud.feature.mediafiles.entityassociation.exception.MediaFileFieldTypeNotMediaFileException
import studio.crud.feature.mediafiles.model.MediaFile
import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaEntity

@Component
class MediaFileEntityFieldResolverImpl : MediaFileEntityFieldResolver {

    private val mediaFileEntityFieldMetadatas: List<MediaFileEntityFieldMetadata>

    private val mediaFileEntityFieldMetadataCache: MutableMap<Pair<String, String>, MediaFileEntityFieldMetadata?> = mutableMapOf()

    init {
        val reflections = Reflections("studio.crud")
        mediaFileEntityFieldMetadatas = reflections.getSubTypesOf(AbstractJpaEntity::class.java).flatMap { clazz ->
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
                        field.getDeclaredAnnotation(MediaFileField::class.java)
                    )
                }
        }

        println()
    }

    override fun getFieldMetadata(entityName: String, fieldName: String): MediaFileEntityFieldMetadata {
        val key = entityName to fieldName
        return mediaFileEntityFieldMetadataCache.computeIfAbsent(key) {
            mediaFileEntityFieldMetadatas.find { it.entitySimpleName == entityName && it.fieldName == fieldName }
        } ?: throw MediaFileFieldNotFoundException(entityName, fieldName)
    }
}

