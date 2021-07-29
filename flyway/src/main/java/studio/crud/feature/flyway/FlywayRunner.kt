package studio.crud.feature.flyway

import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.MigrationVersion
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import javax.sql.DataSource

class FlywayRunner {
    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    @Autowired(required = false)
    private val locationProviders: List<FlywayLocationProvider> = emptyList()

    fun migrate() {
        log.info { "Found ${locationProviders.size} Flyway Location Providers - ${locationProviders.map { it::class.simpleName }.joinToString()}" }
        val providedLocations = (locationProviders.flatMap { it.provide() }).toTypedArray()
        val locations = arrayOf(FLYWAY_DEFAULT_LOCATION, *providedLocations)
        val configuration = FluentConfiguration(resourceLoader.classLoader)
            .dataSource(dataSource)
            .baselineOnMigrate(true)
            .outOfOrder(true)
            .locations(*locations)
            .ignoreMissingMigrations(false)
            .baselineVersion(MigrationVersion.fromVersion("1"))
        val flyway = Flyway(configuration)
        flyway.migrate()
    }

    companion object {
        private val FLYWAY_DEFAULT_LOCATION = "classpath:db/migration"
        private val log = KotlinLogging.logger { }
    }
}