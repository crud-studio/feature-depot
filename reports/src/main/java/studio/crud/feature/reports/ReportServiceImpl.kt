package studio.crud.feature.reports

import org.springframework.stereotype.Service
import studio.crud.feature.reports.model.Report
import studio.crud.feature.reports.ro.ReportDataRO
import studio.crud.sharedcommon.utils.KeyValuePair
import studio.crud.sharedcommon.utils.toMap
import java.util.*

@Service
class ReportServiceImpl(
    private val reportHandler: ReportHandler
) : ReportService {
    override fun generateReport(reportId: Long, parameters: Set<KeyValuePair<String, Array<Any>>>, currentOperatorId: Long): ReportDataRO {
        val report: Report = reportHandler.getReportById(reportId)
        val reportName = report.name.replace(" ", "") + "-" + Date()
        val data = reportHandler.generateReport(report, parameters.toMap())
        return ReportDataRO(reportName, report.headers, data)
    }
}