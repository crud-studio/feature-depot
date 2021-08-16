package studio.crud.feature.dashboard

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.modelfilter.dsl.where
import org.springframework.stereotype.Service
import studio.crud.feature.dashboard.model.DashboardWidget
import studio.crud.feature.dashboard.ro.DashboardDataDTO
import studio.crud.feature.dashboard.widget.DashboardWidgetDataHandler

@Service
class DashboardServiceImpl(
        val crudHandler: CrudHandler,
        val dashboardWidgetDataHandler: DashboardWidgetDataHandler
): DashboardService {
    override fun getDashboardData(widgetIds: List<Long>): List<DashboardDataDTO<*>> {
        val widgets = crudHandler.index(where {
            "id" RequireIn widgetIds
        }, DashboardWidget::class.java)
//                .enforceAccess(Operator::class.java, userInfo.internalId) todo
                .execute()
                .data

        return widgets.map {
            DashboardDataDTO(it.id, it.type, it.titleKey, dashboardWidgetDataHandler.getWidgetData(it))
        }
    }
}