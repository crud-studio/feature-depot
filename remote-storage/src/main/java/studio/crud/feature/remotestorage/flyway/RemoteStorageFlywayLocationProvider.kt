package studio.crud.feature.remotestorage.flyway

import org.springframework.stereotype.Component
import studio.crud.feature.flyway.FlywayLocationProvider

@Component
class RemoteStorageFlywayLocationProvider : FlywayLocationProvider {
    override fun provide(): Set<String> {
        return setOf("classpath:studio/crud/feature/remotestorage/db/migration")
    }
}