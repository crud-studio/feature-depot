package studio.crud.feature.reports.ro

data class ReportDataRO(
    var name: String,
    var headers: List<String>?,
    var data: List<Array<Any>>
)