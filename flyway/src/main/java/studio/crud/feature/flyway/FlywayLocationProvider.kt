package studio.crud.feature.flyway

interface FlywayLocationProvider {
    /**
     * Provide a list of Flyway migration locations
     */
    fun provide(): Set<String>
}