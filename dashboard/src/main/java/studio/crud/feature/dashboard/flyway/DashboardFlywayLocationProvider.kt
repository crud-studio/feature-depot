package studio.crud.feature.dashboard.flyway

import org.springframework.stereotype.Component
import studio.crud.feature.flyway.FlywayLocationProvider

@Component
class DashboardFlywayLocationProvider : FlywayLocationProvider {
    override fun provide(): Set<String> {
        return setOf("classpath:studio/crud/feature/dashboard/db/migration")
    }
}