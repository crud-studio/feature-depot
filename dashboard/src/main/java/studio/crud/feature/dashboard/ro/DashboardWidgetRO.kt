package studio.crud.feature.dashboard.ro

import studio.crud.feature.dashboard.enums.DashboardWidgetType
import studio.crud.feature.jpa.ro.AbstractJpaUpdatableRO

class DashboardWidgetRO(
        var titleKey: String,
        var type: DashboardWidgetType,
        var params: MutableMap<String, String> = mutableMapOf()
) : AbstractJpaUpdatableRO()
