package studio.crud.feature.mediafiles.entityassociation.annotation

import studio.crud.feature.mediafiles.enums.MediaFileAclMode

/**
 * This annotation marks the provided field as a media file field for automatic association
 */
@Target(AnnotationTarget.FIELD)
annotation class MediaFileField(

    /**
     * Allowed file extensions for this field. If empty, allow all
     */
    val allowedExtensions: Array<String> = emptyArray(),

    /**
     * Media File ACL mode for this field
     */
    val aclMode: MediaFileAclMode = MediaFileAclMode.PRIVATE

)


