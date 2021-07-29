package studio.crud.feature.reports

/**
 * Interface for DAO for reports.
 */
interface ReportDao {
    fun runStoredSqlReturnMultiColumn(sql: String, parameters: Map<String, Array<out Any>>, maxResults: Int?): List<Array<Any>>
}