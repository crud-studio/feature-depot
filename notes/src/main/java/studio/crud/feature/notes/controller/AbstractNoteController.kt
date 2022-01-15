package studio.crud.feature.notes.controller

import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.modelfilter.enums.FilterFieldOperation
import org.springframework.beans.factory.annotation.Autowired
import studio.crud.feature.core.objecttyperesolver.ObjectTypeStore
import studio.crud.feature.core.util.getField
import studio.crud.feature.core.web.annotations.CrudOperations
import studio.crud.feature.jpa.web.AbstractSimplifiedErrorHandlingJpaCrudController
import studio.crud.feature.notes.model.Note
import studio.crud.feature.notes.ro.NoteRO
import kotlin.reflect.KClass

@CrudOperations(update = false, delete = false, create = false, show = false)
abstract class AbstractNoteController : AbstractSimplifiedErrorHandlingJpaCrudController<Note, NoteRO>() {
    @Autowired
    private lateinit var objectTypeStore: ObjectTypeStore

    abstract fun getNoteAccessorId(): String

    abstract fun getNoteAccessorType(): KClass<*>

    override fun preSearch(filter: DynamicModelFilter) {
        val targetTypeFilter = filter.getField("targetType", FilterFieldOperation.Equal)
        if(targetTypeFilter != null) {
            targetTypeFilter.values = arrayOf(objectTypeStore.getName(targetTypeFilter.value1.toString()))
        }
        val creatorTypeFilter = filter.getField("creatorType", FilterFieldOperation.Equal)
        if(creatorTypeFilter != null) {
            creatorTypeFilter.values = arrayOf(objectTypeStore.getName(creatorTypeFilter.value1.toString()))
        }
    }

    override fun preCreate(ro: NoteRO) {
        ro.creatorId = getNoteAccessorId()
        ro.creatorType = objectTypeStore.getName(getNoteAccessorType())
        ro.targetType = objectTypeStore.getName(ro.targetType)
    }
}

