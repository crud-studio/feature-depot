package studio.crud.feature.parameters.flyway

import org.springframework.stereotype.Component
import studio.crud.feature.flyway.FlywayLocationProvider

@Component
class ParameterFlywayLocationProvider : FlywayLocationProvider {
    override fun provide(): Set<String> {
        return setOf("classpath:studio/crud/feature/parameters/db/migration")
    }
}