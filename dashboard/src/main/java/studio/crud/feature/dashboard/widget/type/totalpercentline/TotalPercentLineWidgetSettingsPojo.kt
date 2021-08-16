package studio.crud.feature.dashboard.widget.type.totalpercentline

import java.io.Serializable

data class TotalPercentLineWidgetSettingsPojo(
    val totalQuery: String,
    val lineQuery: String
) : Serializable
