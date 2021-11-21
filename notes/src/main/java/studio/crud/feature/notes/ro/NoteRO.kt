package studio.crud.feature.notes.ro

import com.antelopesystem.crudframework.fieldmapper.annotation.DefaultMappingTarget
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
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
    var creatorInfo: NoteEntityInfoRO? = null
    var targetInfo: NoteEntityInfoRO? = null
}
