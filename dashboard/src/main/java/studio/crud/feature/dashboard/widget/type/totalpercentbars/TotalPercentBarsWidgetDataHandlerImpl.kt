package studio.crud.feature.dashboard.widget.type.totalpercentbars

import org.springframework.stereotype.Component
import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.dashboard.model.DashboardWidget
import studio.crud.feature.dashboard.widget.WidgetHelper
import studio.crud.feature.dashboard.widget.type.core.AbstractDashboardWidgetTypeDataHandler

@Component
class TotalPercentBarsWidgetDataHandlerImpl(
    private val widgetHelper: WidgetHelper
) : AbstractDashboardWidgetTypeDataHandler<TotalPercentBarsWidgetResultDTO, TotalPercentBarsWidgetSettingsPojo>() {
    override val getType: DashboardWidgetType = DashboardWidgetType.TotalPercentBars

    override fun getData(widget: DashboardWidget, settings: TotalPercentBarsWidgetSettingsPojo): TotalPercentBarsWidgetResultDTO {
        val total = widgetHelper.runNativeQuery(settings.totalQuery).first() as Number
        val entries = widgetHelper.runNativeQuery(settings.barQuery) as List<Number>
        val lastEntry = entries.last()
        val beforeLastEntry = entries[entries.lastIndex-1]
        val change = (lastEntry.toDouble() - beforeLastEntry.toDouble()) / beforeLastEntry.toDouble() * 100.0

        return TotalPercentBarsWidgetResultDTO(total.toLong(), change, entries)
    }
}