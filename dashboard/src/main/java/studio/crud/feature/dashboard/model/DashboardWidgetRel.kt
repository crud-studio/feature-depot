package studio.crud.feature.dashboard.model

import com.antelopesystem.crudframework.fieldmapper.annotation.DefaultMappingTarget
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import org.hibernate.annotations.Type
import studio.crud.feature.dashboard.ro.DashboardWidgetRelRO
import studio.crud.feature.dashboard.ro.WidthDTO
import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaUpdatableEntity
import studio.crud.sharedcommon.crud.transformer.MappingTransformer
import javax.persistence.*

@Entity
@Table(name = "dashboard_widget_rel")
@DefaultMappingTarget(DashboardWidgetRelRO::class)
class DashboardWidgetRel(

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "dashboard_definition_id")
        var definition: DashboardDefinition,

        @MappedField(transformer = MappingTransformer::class)
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "dashboard_widget_id")
        var widget: DashboardWidget,

        @MappedField
        @Column(nullable = false)
        var sort: Int,

        @MappedField
        @Type(type = "json")
        @Column(nullable = false, columnDefinition = "LONGTEXT")
        var widthData: List<WidthDTO>

) : AbstractJpaUpdatableEntity()
