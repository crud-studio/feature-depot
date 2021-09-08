package studio.crud.feature.reports

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test
import strikt.api.expectThrows
import studio.crud.feature.core.util.KeyValuePair.Companion.toMap
import studio.crud.feature.core.util.KeyValuePair.Companion.vs
import studio.crud.feature.reports.enums.ReportParameterType
import studio.crud.feature.reports.exception.ReportParameterInvalidException
import studio.crud.feature.reports.exception.ReportParameterInvalidValueException
import studio.crud.feature.reports.exception.ReportParameterMissingValueException
import studio.crud.feature.reports.model.Report
import studio.crud.feature.reports.model.ReportParameterDefinitionDTO
class ReportHandlerImplTest {
    private val crudHandler: CrudHandler = mock()
    private val reportDao: ReportDao = mock()
    private val instance = ReportHandlerImpl(crudHandler, reportDao)

    @Test
    fun `get report data should fail validation if given parameter was not defined`() {
        val reportMock = Report()
        val parameters = listOf(
            "operatorId" vs arrayOf(1)
        ).toMap()

        expectThrows<ReportParameterInvalidException> {
            instance.generateReport(reportMock, parameters)
        }
    }

    @Test
    fun `get report data should fail validation if given parameter was given incorrect values`() {
        val reportMock = Report().apply {
            parameterDefinitions = listOf(
                ReportParameterDefinitionDTO(
                    "Operator ID",
                    "operatorId",
                    ReportParameterType.Integer,
                    arrayOf("1")
                )
            )
        }
        val parameters = listOf(
            "operatorId" vs arrayOf(true)
        ).toMap()

        expectThrows<ReportParameterInvalidValueException> {
            instance.generateReport(reportMock, parameters)
        }
    }

    @Test
    fun `get report data should fail validation if given parameter was not given a value and has no default`() {
        val reportMock = Report().apply {
            parameterDefinitions = listOf(
                ReportParameterDefinitionDTO(
                    "Operator ID",
                    "operatorId",
                    ReportParameterType.Integer,
                    null
                )
            )
        }

        expectThrows<ReportParameterMissingValueException> {
            instance.generateReport(reportMock, emptyMap())
        }
    }
}