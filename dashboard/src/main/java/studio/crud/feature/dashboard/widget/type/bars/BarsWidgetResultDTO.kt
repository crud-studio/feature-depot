package studio.crud.feature.dashboard.widget.type.bars

import java.io.Serializable

data class BarsWidgetResultDTO(
        val entries: List<BarsWidgetChartEntryDTO>
): Serializable {
        data class BarsWidgetChartEntryDTO(
                val nameKey: String,
                val result: Number
        )
}