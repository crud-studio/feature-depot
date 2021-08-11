package studio.crud.feature.mediafiles.entityassociation

interface MediaFileEntityFieldResolver {
    fun getFieldMetadata(entityName: String, fieldName: String): MediaFileEntityFieldMetadata
}