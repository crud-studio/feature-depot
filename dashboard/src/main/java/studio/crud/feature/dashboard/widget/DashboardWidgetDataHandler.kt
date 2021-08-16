package studio.crud.feature.dashboard.widget

import studio.crud.feature.dashboard.model.DashboardWidget

interface DashboardWidgetDataHandler {
    fun getWidgetData(widget: DashboardWidget): Any
}