package studio.crud.feature.dashboard.widget.type.lines

import java.io.Serializable

data class LinesWidgetResultDTO(
        val entries: List<LinesWidgetChartEntryDTO>,
        val xLabels: List<String> = emptyList()
): Serializable {
        data class LinesWidgetChartEntryDTO(
                val nameKey: String,
                val result: List<Number>
        )
}