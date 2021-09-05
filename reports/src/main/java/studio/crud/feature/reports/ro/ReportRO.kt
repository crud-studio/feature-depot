package studio.crud.feature.reports.ro

import studio.crud.feature.jpa.ro.AbstractJpaUpdatableRO
import studio.crud.feature.reports.model.ReportParameterDefinitionDTO


class ReportRO : AbstractJpaUpdatableRO() {
    var name: String? = null
    var description: String? = null
    var parameterDefinitions: List<ReportParameterDefinitionDTO>? = null
}
