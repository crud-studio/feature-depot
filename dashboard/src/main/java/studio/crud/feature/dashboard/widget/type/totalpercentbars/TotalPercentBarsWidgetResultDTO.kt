package studio.crud.feature.dashboard.widget.type.totalpercentbars

import java.io.Serializable

data class TotalPercentBarsWidgetResultDTO(
    val total: Long,
    val change: Double,
    val entries: List<Number>
) : Serializable
