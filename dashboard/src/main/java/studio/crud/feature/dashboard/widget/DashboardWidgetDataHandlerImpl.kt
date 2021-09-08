package studio.crud.feature.dashboard.widget

import com.antelopesystem.crudframework.utils.component.componentmap.annotation.ComponentMap
import org.springframework.stereotype.Component
import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.dashboard.exception.NoHandlerFoundForDashboardWidgetTypeException
import studio.crud.feature.dashboard.model.DashboardWidget
import studio.crud.feature.dashboard.widget.type.core.DashboardWidgetTypeDataHandler

@Component
class DashboardWidgetDataHandlerImpl : DashboardWidgetDataHandler {
    @ComponentMap
    private lateinit var widgetTypeDataHandlers: Map<DashboardWidgetType, DashboardWidgetTypeDataHandler<Any>>

    override fun getWidgetData(widget: DashboardWidget): Any {
        val handler = widgetTypeDataHandlers[widget.type] ?: throw NoHandlerFoundForDashboardWidgetTypeException(widget.type)
        return handler.getData(widget)
    }
}