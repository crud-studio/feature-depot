package studio.crud.feature.dashboard

import studio.crud.feature.dashboard.ro.DashboardDataDTO

interface DashboardService {
    fun getDashboardData(widgetIds: List<Long>, accessorId: Long?, accessorType: Class<*>?): List<DashboardDataDTO<*>>
}