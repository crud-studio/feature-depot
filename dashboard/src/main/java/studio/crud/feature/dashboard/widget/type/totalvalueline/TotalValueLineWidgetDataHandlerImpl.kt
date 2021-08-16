package studio.crud.feature.dashboard.widget.type.totalvalueline

import org.springframework.stereotype.Component
import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.dashboard.model.DashboardWidget
import studio.crud.feature.dashboard.widget.WidgetHelper
import studio.crud.feature.dashboard.widget.type.core.AbstractDashboardWidgetTypeDataHandler

@Component
class TotalValueLineWidgetDataHandlerImpl(
    private val widgetHelper: WidgetHelper
) : AbstractDashboardWidgetTypeDataHandler<TotalValueLineWidgetResultDTO, TotalValueLineWidgetSettingsPojo>() {
    override val getType: DashboardWidgetType = DashboardWidgetType.TotalValueLine

    override fun getData(widget: DashboardWidget, settings: TotalValueLineWidgetSettingsPojo): TotalValueLineWidgetResultDTO {
        val total = widgetHelper.runNativeQuery(settings.totalQuery).first() as Number
        val entries = widgetHelper.runNativeQuery(settings.lineQuery) as List<Number>
        val value = widgetHelper.runNativeQuery(settings.valueQuery).first() as Number

        return TotalValueLineWidgetResultDTO(total.toLong(), settings.valueKey, value, entries)
    }
}