package studio.crud.feature.dashboard.widget.type.totalpercentline

import java.io.Serializable

data class TotalPercentLineWidgetResultDTO(
    val total: Long,
    val change: Double,
    val entries: List<Number>
) : Serializable
