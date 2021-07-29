package studio.crud.feature.reports

import studio.crud.feature.reports.exception.ReportNotFoundByIdException
import studio.crud.feature.reports.ro.ReportDataRO
import studio.crud.sharedcommon.utils.KeyValuePair

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