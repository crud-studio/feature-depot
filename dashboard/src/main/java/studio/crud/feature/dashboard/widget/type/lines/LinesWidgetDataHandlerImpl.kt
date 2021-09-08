package studio.crud.feature.dashboard.widget.type.lines

import org.springframework.stereotype.Component
import studio.crud.feature.core.util.KeyValuePair.Companion.vs
import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.dashboard.model.DashboardWidget
import studio.crud.feature.dashboard.widget.WidgetHelper
import studio.crud.feature.dashboard.widget.type.core.AbstractDashboardWidgetTypeDataHandler

@Component
class LinesWidgetDataHandlerImpl(
    private val widgetHelper: WidgetHelper
) : AbstractDashboardWidgetTypeDataHandler<LinesWidgetResultDTO, LinesWidgetSettingsPojo>() {
    override val getType: DashboardWidgetType = DashboardWidgetType.Lines

    override fun getData(widget: DashboardWidget, settings: LinesWidgetSettingsPojo): LinesWidgetResultDTO {
        val entries = settings.entries.map { entry ->
            entry.nameKey vs widgetHelper.runNativeQuery(entry.query)
        }.map {
            LinesWidgetResultDTO.LinesWidgetChartEntryDTO(it.key, it.value as List<Number>)
        }
        return LinesWidgetResultDTO(entries, settings.xLabels ?: emptyList())
    }
}