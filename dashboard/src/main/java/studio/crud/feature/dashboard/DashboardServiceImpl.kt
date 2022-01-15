package studio.crud.feature.dashboard

import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.modelfilter.dsl.where
import org.springframework.stereotype.Service
import studio.crud.feature.dashboard.model.DashboardWidget
import studio.crud.feature.dashboard.ro.DashboardDataDTO
import studio.crud.feature.dashboard.widget.DashboardWidgetDataHandler

@Service
class DashboardServiceImpl(
        val crudHandler: CrudHandler,
        val dashboardWidgetDataHandler: DashboardWidgetDataHandler
): DashboardService {
    override fun getDashboardData(widgetIds: List<Long>, accessorId: Long?, accessorType: Class<*>?): List<DashboardDataDTO<*>> {
        val crudCall = crudHandler.index(where {
            "id" RequireIn widgetIds
        }, DashboardWidget::class.java)
//                .enforceAccess(Operator::class.java, userInfo.internalId) todo
        if(accessorId != null && accessorType != null) {
            crudCall.enforceAccess(accessorType, accessorId)
        }

        return crudCall.execute().data.map {
            DashboardDataDTO(it.id, it.type, it.titleKey, dashboardWidgetDataHandler.getWidgetData(it))
        }
    }
}