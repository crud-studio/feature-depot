package studio.crud.feature.reports.ro

import studio.crud.feature.reports.model.ReportParameterDefinitionDTO
import studio.crud.sharedcommon.crud.jpa.ro.AbstractJpaRO


class ReportRO : AbstractJpaRO() {
    var name: String? = null
    var description: String? = null
    var parameterDefinitions: List<ReportParameterDefinitionDTO>? = null
}
