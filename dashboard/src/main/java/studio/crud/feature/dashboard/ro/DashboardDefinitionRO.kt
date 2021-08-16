package studio.crud.feature.dashboard.ro

import studio.crud.sharedcommon.crud.jpa.ro.AbstractJpaUpdatableRO

open class DashboardDefinitionRO(
        var titleKey: String,
        var tag: String,
        var widgets: List<DashboardWidgetRelRO>
) : AbstractJpaUpdatableRO()

