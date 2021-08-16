package studio.crud.feature.dashboard.widget.type.datatable

import org.springframework.stereotype.Component
import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.dashboard.model.DashboardWidget
import studio.crud.feature.dashboard.widget.WidgetHelper
import studio.crud.feature.dashboard.widget.type.core.AbstractDashboardWidgetTypeDataHandler

@Component
class DataTableWidgetDataHandlerImpl(
    private val widgetHelper: WidgetHelper
) : AbstractDashboardWidgetTypeDataHandler<DataTableWidgetResultDTO, DataTableWidgetSettingsPojo>() {
    override val getType: DashboardWidgetType = DashboardWidgetType.DataTable

    override fun getData(widget: DashboardWidget, settings: DataTableWidgetSettingsPojo): DataTableWidgetResultDTO {
        val result = widgetHelper.runNativeQuery(settings.query)
        return DataTableWidgetResultDTO(settings.headers, result as List<Array<Any>>, settings.viewAllUrl)
    }
}