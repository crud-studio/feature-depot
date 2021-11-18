package studio.crud.feature.notes.flyway

import org.springframework.stereotype.Component
import studio.crud.feature.flyway.FlywayLocationProvider

@Component
class NotesFlywayLocationProvider : FlywayLocationProvider {
    override fun provide(): Set<String> {
        return setOf("classpath:studio/crud/feature/notes/db/migration")
    }
}