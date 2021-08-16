package studio.crud.feature.dashboard.widget.type.totalvaluebars

import java.io.Serializable

data class TotalValueBarsWidgetResultDTO(
    val total: Long,
    val valueKey: String,
    val value: Number,
    val entries: List<Number>
) : Serializable
