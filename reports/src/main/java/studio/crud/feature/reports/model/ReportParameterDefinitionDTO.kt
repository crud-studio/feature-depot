package studio.crud.feature.reports.model

import studio.crud.feature.reports.enums.ReportParameterType
import java.io.Serializable

class ReportParameterDefinitionDTO(
    var title: String,
    var name: String,
    var type: ReportParameterType,
    var defaultValue: Array<String>?
) : Serializable
