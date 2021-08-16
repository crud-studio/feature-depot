package studio.crud.feature.dashboard.widget.type.core

import com.antelopesystem.crudframework.utils.component.componentmap.annotation.ComponentMapKey
import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.dashboard.model.DashboardWidget

interface DashboardWidgetTypeDataHandler<ResultType : Any> {
    @get:ComponentMapKey
    val getType: DashboardWidgetType

    fun getData(widget: DashboardWidget): ResultType

}