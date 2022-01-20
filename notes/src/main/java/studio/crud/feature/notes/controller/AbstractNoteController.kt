package studio.crud.feature.notes.controller

import org.springframework.beans.factory.annotation.Autowired
import studio.crud.feature.core.entity.EntityTypeHandlerStore
import studio.crud.feature.core.web.annotations.CrudOperations
import studio.crud.feature.jpa.web.AbstractSimplifiedErrorHandlingJpaCrudController
import studio.crud.feature.notes.model.Note
import studio.crud.feature.notes.ro.NoteRO

@CrudOperations(update = false, delete = false, show = false)
abstract class AbstractNoteController : AbstractSimplifiedErrorHandlingJpaCrudController<Note, NoteRO>() {
    @Autowired
    private lateinit var entityTypeHandlerStore: EntityTypeHandlerStore

    override fun preCreate(ro: NoteRO) {
        val accessorId = accessorId() ?: return
        val accessorType = accessorType() ?: return
        ro.creatorId = accessorId.toString()
        ro.creatorType = entityTypeHandlerStore.getTypeForClazz(accessorType.kotlin)
    }
}

