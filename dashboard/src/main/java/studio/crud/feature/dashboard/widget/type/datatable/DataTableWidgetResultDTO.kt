package studio.crud.feature.dashboard.widget.type.datatable

import java.io.Serializable

data class DataTableWidgetResultDTO(
        val headers: List<String>,
        val data: List<Array<Any>>,
        val viewAllUrl: String?
): Serializable