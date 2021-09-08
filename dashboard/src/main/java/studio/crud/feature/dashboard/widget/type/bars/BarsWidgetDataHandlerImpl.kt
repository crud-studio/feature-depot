package studio.crud.feature.dashboard.widget.type.bars

import org.springframework.stereotype.Component
import studio.crud.feature.core.util.KeyValuePair.Companion.vs
import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.dashboard.model.DashboardWidget
import studio.crud.feature.dashboard.widget.WidgetHelper
import studio.crud.feature.dashboard.widget.type.core.AbstractDashboardWidgetTypeDataHandler

@Component
class BarsWidgetDataHandlerImpl(
    private val widgetHelper: WidgetHelper
) : AbstractDashboardWidgetTypeDataHandler<BarsWidgetResultDTO, BarsWidgetSettingsPojo>() {
    override val getType: DashboardWidgetType = DashboardWidgetType.Bars

    override fun getData(widget: DashboardWidget, settings: BarsWidgetSettingsPojo): BarsWidgetResultDTO {
        val entries = settings.entries.map { entry ->
            entry.nameKey vs widgetHelper.runNativeQuery(entry.query)
        }.map {
            BarsWidgetResultDTO.BarsWidgetChartEntryDTO(it.key, it.value.first() as Number)
        }
        return BarsWidgetResultDTO(entries)
    }
}