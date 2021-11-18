package studio.crud.feature.notes.model

import com.antelopesystem.crudframework.crud.annotation.Immutable
import com.antelopesystem.crudframework.fieldmapper.annotation.DefaultMappingTarget
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import studio.crud.feature.jpa.model.AbstractJpaUpdatableEntity
import studio.crud.feature.notes.ro.NoteRO
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table
@DefaultMappingTarget(NoteRO::class)
@Immutable
class Note(
    @MappedField
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    var content: String,

    @MappedField
    @Column(nullable = false, length = 1024)
    var creatorType: String,

    @MappedField
    @Column(nullable = false, length = 1024)
    var creatorId: String,

    @MappedField
    @Column(nullable = false, length = 1024)
    var targetType: String,

    @MappedField
    @Column(nullable = false, length = 1024)
    var targetId: String
) : AbstractJpaUpdatableEntity()