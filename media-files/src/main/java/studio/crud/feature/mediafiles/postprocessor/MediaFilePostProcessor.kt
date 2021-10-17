package studio.crud.feature.mediafiles.postprocessor

import studio.crud.feature.mediafiles.model.MediaFile

/**
 * Represents a post processor which runs after file download, but before returning to the client
 */
interface MediaFilePostProcessor {
    /**
     * Set of file extensions supported by this post processor
     */
    fun supportedExtensions(): Set<String>

    /**
     * Set of parameters supported by this post processor
     */
    fun supportedParameters(): Set<String>

    /**
     * The priority of this post processor, higher is better
     */
    fun priority(): Int = Int.MIN_VALUE

    /**
     * Run the post processor
     */
    fun run(mediaFile: MediaFile, bytes: ByteArray, parameters: Map<String, List<String>>): ByteArray
}