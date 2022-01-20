package studio.crud.feature.notes.crud

import studio.crud.crudframework.crud.decorator.ObjectDecoratorBase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import studio.crud.feature.core.entity.EntityTypeHandlerStore
import studio.crud.feature.notes.model.Note
import studio.crud.feature.notes.ro.NoteRO

@Component
class NoteNoteRODecorator(
    @Autowired(required = false)
    private val entityTypeHandlerStore: EntityTypeHandlerStore
) : ObjectDecoratorBase<Note, NoteRO>() {
    override fun decorate(fromObject: Note, toObject: NoteRO) {
        toObject.creatorInfo = entityTypeHandlerStore.getDisplayRO(fromObject.creatorType, fromObject.creatorId.toLong())
        toObject.targetInfo = entityTypeHandlerStore.getDisplayRO(fromObject.targetType, fromObject.targetId.toLong())
    }
}