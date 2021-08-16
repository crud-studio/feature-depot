package studio.crud.feature.dashboard.widget.type.totalpercentline

import org.springframework.stereotype.Component
import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.dashboard.model.DashboardWidget
import studio.crud.feature.dashboard.widget.WidgetHelper
import studio.crud.feature.dashboard.widget.type.core.AbstractDashboardWidgetTypeDataHandler

@Component
class TotalPercentLineWidgetDataHandlerImpl(
    private val widgetHelper: WidgetHelper
) : AbstractDashboardWidgetTypeDataHandler<TotalPercentLineWidgetResultDTO, TotalPercentLineWidgetSettingsPojo>() {
    override val getType: DashboardWidgetType = DashboardWidgetType.TotalPercentLine

    override fun getData(widget: DashboardWidget, settings: TotalPercentLineWidgetSettingsPojo): TotalPercentLineWidgetResultDTO {
        val total = widgetHelper.runNativeQuery(settings.totalQuery).first() as Number
        val entries = widgetHelper.runNativeQuery(settings.lineQuery) as List<Number>
        val lastEntry = entries.last()
        val beforeLastEntry = entries[entries.lastIndex-1]
        val change = (lastEntry.toDouble() - beforeLastEntry.toDouble()) / beforeLastEntry.toDouble() * 100.0

        return TotalPercentLineWidgetResultDTO(total.toLong(), change, entries)
    }
}