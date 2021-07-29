package studio.crud.feature.mediafiles.hooks

import studio.crud.feature.mediafiles.exception.MediaFileAccessDeniedException
import studio.crud.feature.mediafiles.model.MediaFile

interface MediaFileDownloadHooks {
    /**
     * Called before the media file
     * @throws MediaFileAccessDeniedException
     */
    fun preDownload(mediaFile: MediaFile) {}

    /**
     * Called after the media file has been downloaded
     */
    fun postDownload(mediaFile: MediaFile, bytes: ByteArray) {}
}