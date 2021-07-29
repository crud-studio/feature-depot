package studio.crud.feature.reports.model

import com.antelopesystem.crudframework.crud.annotation.Immutable
import com.antelopesystem.crudframework.fieldmapper.annotation.DefaultMappingTarget
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import org.hibernate.annotations.Type
import studio.crud.feature.reports.ro.ReportRO
import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaUpdatableEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table


@Entity
@Table
//@PermissionedEntity(
//    create = PermissionedEntity.PermissionGenerationMode.NEVER,
//    update = PermissionedEntity.PermissionGenerationMode.NEVER,
//    delete = PermissionedEntity.PermissionGenerationMode.NEVER
//)
@Immutable
@DefaultMappingTarget(ReportRO::class)
class Report : AbstractJpaUpdatableEntity() {
    @MappedField
    @Column(nullable = false)
    var name: String = ""

    @MappedField
    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String = ""

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    var sqlQuery: String = ""

    @MappedField
    @Column(nullable = true, columnDefinition = "LONGTEXT")
    @Type(type = "json")
    var parameterDefinitions: List<ReportParameterDefinitionDTO> = listOf()

    @Column(nullable = true, columnDefinition = "LONGTEXT")
    @Type(type = "json")
    var headers: List<String>? = null

    @Column(nullable = true)
    var rowLimit: Int? = null
}
