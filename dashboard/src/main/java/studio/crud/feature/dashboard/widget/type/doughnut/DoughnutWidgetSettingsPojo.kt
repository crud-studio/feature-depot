package studio.crud.feature.dashboard.widget.type.doughnut

import java.io.Serializable

data class DoughnutWidgetSettingsPojo(
        val entries: List<DoughnutWidgetEntrySettingsPojo>
): Serializable {
        data class DoughnutWidgetEntrySettingsPojo(
                val nameKey: String,
                val query: String
        )
}