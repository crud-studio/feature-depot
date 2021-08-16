package studio.crud.feature.dashboard.widget.type.bars

import java.io.Serializable

data class BarsWidgetSettingsPojo(
        val entries: List<BarsWidgetEntrySettingsPojo>
): Serializable {
        data class BarsWidgetEntrySettingsPojo(
                val nameKey: String,
                val query: String
        )
}