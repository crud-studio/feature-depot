package studio.crud.feature.dashboard.widget.type.datatable

import java.io.Serializable

data class DataTableWidgetSettingsPojo(
        val headers: List<String>,
        val query: String,
        val viewAllUrl: String?
): Serializable