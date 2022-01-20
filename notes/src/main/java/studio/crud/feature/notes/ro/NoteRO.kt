package studio.crud.feature.notes.ro

import studio.crud.crudframework.fieldmapper.annotation.DefaultMappingTarget
import studio.crud.crudframework.fieldmapper.annotation.MappedField
import studio.crud.feature.core.entity.EntityDisplayRO
import studio.crud.feature.jpa.ro.AbstractJpaUpdatableRO
import studio.crud.feature.notes.model.Note

@DefaultMappingTarget(Note::class)
class NoteRO(
    @MappedField
    var content: String = "",

    @MappedField
    var targetType: String = "",

    @MappedField
    var targetId: String = ""
) : AbstractJpaUpdatableRO() {
    @MappedField
    var creatorType: String? = null

    @MappedField
    var creatorId: String? = null

    var creatorInfo: EntityDisplayRO? = null

    var targetInfo: EntityDisplayRO? = null
}

