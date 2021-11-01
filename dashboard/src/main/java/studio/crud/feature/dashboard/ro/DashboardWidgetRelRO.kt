package studio.crud.feature.dashboard.ro

import studio.crud.feature.jpa.ro.AbstractJpaUpdatableRO

class DashboardWidgetRelRO(
        var widget: DashboardWidgetRO? = null,
        var sort: Int? = null,
        var widthData: List<WidthDTO> = emptyList()
) : AbstractJpaUpdatableRO()
