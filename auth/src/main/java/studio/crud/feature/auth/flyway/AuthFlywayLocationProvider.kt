package studio.crud.feature.auth.flyway

import org.springframework.stereotype.Component
import studio.crud.feature.flyway.FlywayLocationProvider

@Component
class AuthFlywayLocationProvider : FlywayLocationProvider {
    override fun provide(): Set<String> {
        return setOf("classpath:studio/crud/feature/auth/db/migration")
    }
}