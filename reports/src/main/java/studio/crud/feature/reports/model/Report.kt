package studio.crud.feature.reports.model

import studio.crud.crudframework.crud.annotation.Immutable
import studio.crud.crudframework.fieldmapper.annotation.DefaultMappingTarget
import studio.crud.crudframework.fieldmapper.annotation.MappedField
import org.hibernate.annotations.Type
import studio.crud.feature.jpa.model.AbstractJpaUpdatableEntity
import studio.crud.feature.reports.ro.ReportRO
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table


@Entity
@Table
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
