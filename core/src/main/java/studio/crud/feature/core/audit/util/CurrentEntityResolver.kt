package studio.crud.feature.core.audit.util

interface CurrentEntityResolver {
    /**
     * Resolve the current entity (eg user)
     * TODO: refactor to a more generic user resolver
     */
    fun resolve(): ResolvedEntity?

    data class ResolvedEntity(
        val objectId: String,
        val objectType: String
    ) {
        companion object {
            fun from(objectId: String?, objectType: String?): ResolvedEntity? {
                objectId ?: return null
                objectType ?: return null
                return ResolvedEntity(objectId, objectType)
            }
        }
    }
}