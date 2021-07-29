package studio.crud.feature.mediafiles.hooks

interface MediaFileCreatorResolver {
    /**
     * Resolve the creator of a Media File
     * TODO: refactor to a more generic user resolver
     */
    fun resolve(): ResolvedCreator?

    data class ResolvedCreator(
        val objectId: Long,
        val objectType: String
    ) {
        companion object {
            fun from(objectId: Long?, objectType: String?): ResolvedCreator? {
                objectId ?: return null
                objectType ?: return null
                return ResolvedCreator(objectId, objectType)
            }
        }
    }
}