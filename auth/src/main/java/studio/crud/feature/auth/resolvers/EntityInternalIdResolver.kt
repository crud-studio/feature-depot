package studio.crud.feature.auth.resolvers

interface EntityInternalIdResolver {

    /**
     * Resolve an entity's internal ID from the Auth Service UUID
     */
    fun resolve(entityUuid: String): Long
}