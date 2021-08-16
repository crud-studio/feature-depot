package studio.crud.feature.dashboard.widget.type.totalvaluebars

import java.io.Serializable

data class TotalValueBarsWidgetSettingsPojo(
    val totalQuery: String,
    val barQuery: String,
    val valueKey: String,
    val valueQuery: String
) : Serializable