package studio.crud.feature.dashboard.widget.type.totalvalueline

import java.io.Serializable

data class TotalValueLineWidgetResultDTO(
    val total: Long,
    val valueKey: String,
    val value: Number,
    val entries: List<Number>
) : Serializable
