package studio.crud.feature.mediafiles.exception

import studio.crud.sharedcommon.exception.AbstractResourceNotFoundByIdException
import studio.crud.sharedcommon.exception.AbstractResourceNotFoundByUuidException
import studio.crud.feature.exceptionhandling.core.ExceptionMetadata
import studio.crud.feature.exceptionhandling.core.ExceptionParam
import studio.crud.feature.exceptionhandling.core.ServerException

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