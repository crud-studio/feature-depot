package studio.crud.feature.dashboard.model

import com.antelopesystem.crudframework.crud.annotation.Immutable
import com.antelopesystem.crudframework.fieldmapper.annotation.DefaultMappingTarget
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import studio.crud.feature.dashboard.ro.DashboardDefinitionRO
import studio.crud.feature.jpa.model.AbstractJpaUpdatableEntity
import studio.crud.sharedcommon.crud.transformer.CollectionMappingTransformer
import javax.persistence.*

@Entity
@Table(name = "dashboard_definition")
@DefaultMappingTarget(DashboardDefinitionRO::class)
@Immutable
class DashboardDefinition(
        @MappedField
        @Column(nullable = false)
        var titleKey: String,

        @MappedField
        @Column(nullable = false)
        var tag: String,

        @MappedField(transformer = CollectionMappingTransformer::class)
        @Fetch(FetchMode.SELECT)
        @OneToMany(fetch = FetchType.EAGER, mappedBy = "definition")
        var widgets: List<DashboardWidgetRel> = emptyList()
) : AbstractJpaUpdatableEntity()
