package studio.crud.feature.dashboard.widget.type.doughnut

import org.springframework.stereotype.Component
import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.dashboard.model.DashboardWidget
import studio.crud.feature.dashboard.widget.WidgetHelper
import studio.crud.feature.dashboard.widget.type.core.AbstractDashboardWidgetTypeDataHandler
import studio.crud.sharedcommon.utils.vs

@Component
class DoughnutWidgetDataHandlerImpl(
    private val widgetHelper: WidgetHelper
) : AbstractDashboardWidgetTypeDataHandler<DoughnutWidgetResultDTO, DoughnutWidgetSettingsPojo>() {
    override val getType: DashboardWidgetType = DashboardWidgetType.Doughnut

    override fun getData(widget: DashboardWidget, settings: DoughnutWidgetSettingsPojo): DoughnutWidgetResultDTO {
        val entries = settings.entries.map { entry ->
            entry.nameKey vs widgetHelper.runNativeQuery(entry.query)
        }.map {
            DoughnutWidgetResultDTO.DoughnutWidgetChartEntryDTO(it.key, it.value.first() as Number)
        }
        return DoughnutWidgetResultDTO(entries)
    }
}