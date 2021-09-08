package studio.crud.feature.reports

import studio.crud.feature.core.util.KeyValuePair
import studio.crud.feature.reports.exception.ReportNotFoundByIdException
import studio.crud.feature.reports.ro.ReportDataRO

/**
 * Service for reports data.
 */
interface ReportService {

    /**
     * Generates a report by the given [reportId] and [parameters]
     * @throws ReportNotFoundByIdException if the report is not found
     */
    fun generateReport(reportId: Long, parameters: Set<KeyValuePair<String, Array<Any>>>, currentOperatorId: Long): ReportDataRO
}