package studio.crud.feature.dashboard.ro

import studio.crud.feature.dashboard.enums.DashboardWidgetType
import java.io.Serializable

data class DashboardDataDTO<Payload>(
        val dashboardWidgetId: Long,
        val dashboardWidgetType: DashboardWidgetType,
        val dashboardWidgetTitleKey: String,
        val data: Payload
): Serializable