package studio.crud.feature.dashboard.widget.type.lines

import java.io.Serializable

data class LinesWidgetSettingsPojo(
        val entries: List<LinesWidgetEntrySettingsPojo>,
        val xLabels: List<String>?
): Serializable {
        data class LinesWidgetEntrySettingsPojo(
                val nameKey: String,
                val query: String
        )
}