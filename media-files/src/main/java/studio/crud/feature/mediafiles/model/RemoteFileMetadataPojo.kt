package studio.crud.feature.mediafiles.model

data class RemoteFileMetadataPojo(
    /**
     * Remote Filename
     */
    val name: String,

    /**
     * File size in bytes
     */
    val size: Long,

    /**
     * The storage location of the file, generally where the file is at an abstract level.
     * For example, an S3 implementation may mark the location as <bucket_name>
     * When attempting to download the file, if the bucket location changed or if S3 is not used at all, the user will be adequately alerted
     */
    val location: String
)
