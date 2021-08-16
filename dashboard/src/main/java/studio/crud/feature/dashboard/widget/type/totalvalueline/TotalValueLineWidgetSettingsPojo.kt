package studio.crud.feature.dashboard.widget.type.totalvalueline

import java.io.Serializable

data class TotalValueLineWidgetSettingsPojo(
    val totalQuery: String,
    val lineQuery: String,
    val valueKey: String,
    val valueQuery: String
) : Serializable