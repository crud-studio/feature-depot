package studio.crud.feature.mediafiles.entityassociation.exception

import studio.crud.sharedcommon.exception.core.ExceptionMetadata
import studio.crud.sharedcommon.exception.core.ExceptionParam
import studio.crud.sharedcommon.exception.core.ServerException

@ExceptionMetadata(
    params = [
        ExceptionParam("clazzName"),
        ExceptionParam("fieldName")
    ]
)
class MediaFileFieldNotFoundException(val clazzName: String, val fieldName: String) : ServerException("Media File Field [ $fieldName ] was not found on class [ $clazzName ]")

@ExceptionMetadata(
    params = [
        ExceptionParam("entityName"),
        ExceptionParam("fieldName")
    ]
)
class MediaFileFieldTypeNotMediaFileException(val entityName: String, val fieldName: String) : ServerException("Found @MediaFileField annotation on a non-MediaFile field [ $fieldName ] of entity [ $entityName ]")

@ExceptionMetadata(
    params = [
        ExceptionParam("entityName"),
        ExceptionParam("entityId")
    ]
)
class MediaFileEntityNotFoundException(val entityId: Long, val entityName: String) : ServerException("Entity of type [ $entityName ] with ID [ $entityId ] not found")

@ExceptionMetadata(
    params = [
        ExceptionParam("extension"),
        ExceptionParam("allowedExtensions")
    ]
)
class UnauthorizedFileExtensionException(val extension: String, val allowedExtensions: Set<String>) : ServerException("Not allowed to upload files of extensions [ $extension ] - Authorized extensions [ ${allowedExtensions.joinToString()} ]")