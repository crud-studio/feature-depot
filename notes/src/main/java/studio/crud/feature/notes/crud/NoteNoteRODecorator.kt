package studio.crud.feature.notes.crud

import studio.crud.crudframework.crud.decorator.ObjectDecoratorBase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import studio.crud.feature.notes.model.Note
import studio.crud.feature.notes.resolver.DefaultNoteEntityResolver
import studio.crud.feature.notes.resolver.NoteEntityResolver
import studio.crud.feature.notes.ro.NoteRO

@Component
class NoteNoteRODecorator(
    @Autowired(required = false)
    private val noteEntityResolver: NoteEntityResolver?
) : ObjectDecoratorBase<Note, NoteRO>() {
    override fun decorate(fromObject: Note, toObject: NoteRO) {
        val resolver = noteEntityResolver ?: DefaultNoteEntityResolver
        toObject.creatorInfo = resolver.getInfo(fromObject.creatorType, fromObject.creatorId)
        toObject.targetInfo = resolver.getInfo(fromObject.targetType, fromObject.targetId)
    }
}