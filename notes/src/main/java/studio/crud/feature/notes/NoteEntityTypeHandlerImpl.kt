package studio.crud.feature.notes

import org.springframework.stereotype.Component
import studio.crud.feature.core.entity.EntityDisplayRO
import studio.crud.feature.core.entity.EntityTypeHandler
import studio.crud.feature.notes.model.Note
import kotlin.reflect.KClass

@Component
class NoteEntityTypeHandlerImpl : EntityTypeHandler<Note> {
    override val type: String = Note::class.simpleName!!
    override val entityClazz: KClass<Note> = Note::class

    override fun getDisplayRO(entity: Note): EntityDisplayRO {
        return EntityDisplayRO(
            entity.id,
            "Note"
        )
    }
}