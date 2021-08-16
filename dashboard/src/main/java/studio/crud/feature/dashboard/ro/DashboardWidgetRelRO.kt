package studio.crud.feature.dashboard.ro

import studio.crud.sharedcommon.crud.jpa.ro.AbstractJpaUpdatableRO

class DashboardWidgetRelRO(
        var widget: DashboardWidgetRO,
        var sort: Int,
        var widthData: List<WidthDTO>
) : AbstractJpaUpdatableRO()
