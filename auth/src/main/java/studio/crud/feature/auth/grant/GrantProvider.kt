package studio.crud.feature.auth.grant

/**
 * Provides granted authorities
 */
interface GrantProvider {
    /**
     * Provides granted authorities for the given entity by its internal ID
     */
    fun provide(internalEntityId: Long): Set<String>
}