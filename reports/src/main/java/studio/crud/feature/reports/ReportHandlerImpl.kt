package studio.crud.feature.reports

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import studio.crud.feature.reports.enums.ReportParameterType
import studio.crud.feature.reports.exception.ReportNotFoundByIdException
import studio.crud.feature.reports.exception.ReportParameterInvalidException
import studio.crud.feature.reports.exception.ReportParameterInvalidValueException
import studio.crud.feature.reports.exception.ReportParameterMissingValueException
import studio.crud.feature.reports.model.Report
import studio.crud.feature.reports.model.ReportParameterDefinitionDTO
import java.util.*

@Component
class ReportHandlerImpl(
    private val crudHandler: CrudHandler,
    private val reportDao: ReportDao
) : ReportHandler {
    override fun getReportById(reportId: Long): Report {
        return crudHandler.show(reportId, Report::class.java)
            .execute() ?: throw ReportNotFoundByIdException(reportId)
    }

    @Transactional(readOnly = true)
    override fun generateReport(report: Report, parameters: Map<String, Array<out Any>>): List<Array<Any>> {
        var maxResults: Int =  10000 /// parameterHandler.getIntegerParameter("reportMaxResults", 10000)
        if (report.rowLimit != null && report.rowLimit!! > 0) {
            maxResults = report.rowLimit!!
        }
        val finalParameters = getAndValidateParameters(report, parameters)
        return reportDao.runStoredSqlReturnMultiColumn(report.sqlQuery, finalParameters, maxResults)
    }

    private fun getAndValidateParameters(report: Report, parameters: Map<String, Array<out Any>>): Map<String, Array<out Any>> {
        val finalParameters = mutableMapOf<String, Array<out Any>>()
        for ((key, values) in parameters) {
            val parameterDefinition = report.parameterDefinitions.find { it.name == key }
            if(parameterDefinition == null) {
                throw ReportParameterInvalidException(key)
            }
            finalParameters[key] = castParameterValue(parameterDefinition, values)
        }
        for (parameterDefinition in report.parameterDefinitions) {
            if(parameterDefinition.name !in finalParameters) {
                if(parameterDefinition.defaultValue == null) {
                    throw ReportParameterMissingValueException(parameterDefinition.name)
                } else {
                    finalParameters[parameterDefinition.name] = castParameterValue(parameterDefinition, parameterDefinition.defaultValue!!)
                }
            }
        }
        return finalParameters
    }

    private fun castParameterValue(parameterDefinition: ReportParameterDefinitionDTO, values: Array<out Any>): Array<out Any> {
        try {
            return when(parameterDefinition.type) {
                ReportParameterType.String -> values.map { it.toString() }.toTypedArray()
                ReportParameterType.Integer -> values.map { it.toString().toInt() }.toTypedArray()
                ReportParameterType.Boolean -> values.map { it.toString().toBoolean() }.toTypedArray()
                ReportParameterType.Double -> values.map { it.toString().toDouble() }.toTypedArray()
                ReportParameterType.Date -> values.map { Date(it.toString().toLong()) }.toTypedArray()
            }
        } catch(e: Exception) {
            throw ReportParameterInvalidValueException(parameterDefinition.name, values.joinToString())
        }
    }
}