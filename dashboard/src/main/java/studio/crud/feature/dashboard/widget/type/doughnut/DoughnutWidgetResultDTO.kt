package studio.crud.feature.dashboard.widget.type.doughnut

import java.io.Serializable

data class DoughnutWidgetResultDTO(
        val entries: List<DoughnutWidgetChartEntryDTO>
): Serializable {
        data class DoughnutWidgetChartEntryDTO(
                val nameKey: String,
                val result: Number
        )
}