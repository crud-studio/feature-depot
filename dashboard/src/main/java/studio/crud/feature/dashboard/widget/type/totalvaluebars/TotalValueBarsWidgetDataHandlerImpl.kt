package studio.crud.feature.dashboard.widget.type.totalvaluebars

import org.springframework.stereotype.Component
import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.dashboard.model.DashboardWidget
import studio.crud.feature.dashboard.widget.WidgetHelper
import studio.crud.feature.dashboard.widget.type.core.AbstractDashboardWidgetTypeDataHandler

@Component
class TotalValueBarsWidgetDataHandlerImpl(
    private val widgetHelper: WidgetHelper
) : AbstractDashboardWidgetTypeDataHandler<TotalValueBarsWidgetResultDTO, TotalValueBarsWidgetSettingsPojo>() {
    override val getType: DashboardWidgetType = DashboardWidgetType.TotalValueBars

    override fun getData(widget: DashboardWidget, settings: TotalValueBarsWidgetSettingsPojo): TotalValueBarsWidgetResultDTO {
        val total = widgetHelper.runNativeQuery(settings.totalQuery).first() as Number
        val entries = widgetHelper.runNativeQuery(settings.barQuery) as List<Number>
        val value = widgetHelper.runNativeQuery(settings.valueQuery).first() as Number

        return TotalValueBarsWidgetResultDTO(total.toLong(), settings.valueKey, value, entries)
    }
}