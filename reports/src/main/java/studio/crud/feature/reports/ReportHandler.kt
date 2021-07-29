package studio.crud.feature.reports

import studio.crud.feature.reports.exception.ReportNotFoundByIdException
import studio.crud.feature.reports.model.Report

/**
 * Handler for reports data.
 */
interface ReportHandler {

    /**
     * Returns a report by the given [reportId]
     * @throws ReportNotFoundByIdException if the report is not found
     */
    fun getReportById(reportId: Long): Report

    /**
     * Runs a report query.
     * @param report of report.
     * @param limit
     * @return data of sql.
     */
    fun generateReport(report: Report, parameters: Map<String, Array<out Any>>): List<Array<Any>>
}