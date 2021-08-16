package studio.crud.feature.dashboard.widget.type.totalpercentbars

import java.io.Serializable

data class TotalPercentBarsWidgetSettingsPojo(
    val totalQuery: String,
    val barQuery: String
) : Serializable
