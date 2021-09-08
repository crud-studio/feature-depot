package studio.crud.feature.mediafiles.exception

import studio.crud.feature.core.exception.AbstractResourceNotFoundByIdException
import studio.crud.feature.core.exception.AbstractResourceNotFoundByUuidException
import studio.crud.feature.core.exception.ServerException
import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.annotation.ExceptionParam

@ExceptionMetadata(
    params = [
        ExceptionParam("reason")
    ]
)
class MediaFileUploadFailedException(val reason: String? = "N/A") : ServerException(
    "Media File upload failed - $reason"
)

@ExceptionMetadata(
        params = [
            ExceptionParam("reason")
        ]
)
class MediaFileDownloadFailedException(val reason: String? = "N/A") : ServerException(
    "Media File download failed - $reason"
)

@ExceptionMetadata
class MediaFileLocationUnavailableException : ServerException("Cannot download: Media location is not accessible at this moment")

@ExceptionMetadata(
    params = [
        ExceptionParam("mediaFileUuid")
    ]
)
class MediaFileNotFoundByUuidException(val mediaFileUuid: String) : AbstractResourceNotFoundByUuidException(mediaFileUuid, "MediaFile")

@ExceptionMetadata(
    params = [
        ExceptionParam("mediaFileId")
    ]
)
class MediaFileNotFoundByIdException(val mediaFileId: Long) : AbstractResourceNotFoundByIdException(mediaFileId, "MediaFile")

@ExceptionMetadata
class MediaFileAccessDeniedException : ServerException("Access denied to Media File")