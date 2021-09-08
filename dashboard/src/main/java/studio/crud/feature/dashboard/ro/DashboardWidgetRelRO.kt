package studio.crud.feature.dashboard.ro

import studio.crud.feature.jpa.ro.AbstractJpaUpdatableRO

class DashboardWidgetRelRO(
        var widget: DashboardWidgetRO,
        var sort: Int,
        var widthData: List<WidthDTO>
) : AbstractJpaUpdatableRO()
