package studio.crud.feature.dashboard.model

import com.antelopesystem.crudframework.crud.annotation.Immutable
import com.antelopesystem.crudframework.fieldmapper.annotation.DefaultMappingTarget
import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import org.hibernate.annotations.Type
import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.dashboard.ro.DashboardWidgetRO
import studio.crud.feature.jpa.model.AbstractJpaUpdatableEntity
import javax.persistence.*

@Entity
@Table(name = "dashboard_widget")
@DefaultMappingTarget(DashboardWidgetRO::class)
@Immutable
class DashboardWidget(

        @MappedField
        @Column(nullable = false)
        var titleKey: String,

        @MappedField
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var type: DashboardWidgetType,

        /**
         * Client params
         */
        @MappedField
        @Type(type = "json")
        @Column(nullable = true, columnDefinition = "LONGTEXT")
        var params: MutableMap<String, String> = mutableMapOf(),

        /**
         * JSON string for this widget's settings
         */
        @Column(columnDefinition = "LONGTEXT")
        var settings: String?
) : AbstractJpaUpdatableEntity()
