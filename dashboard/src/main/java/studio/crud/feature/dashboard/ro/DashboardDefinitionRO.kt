package studio.crud.feature.dashboard.ro

import studio.crud.feature.jpa.ro.AbstractJpaUpdatableRO


open class DashboardDefinitionRO(
        var titleKey: String,
        var tag: String,
        var widgets: List<DashboardWidgetRelRO>
) : AbstractJpaUpdatableRO()

