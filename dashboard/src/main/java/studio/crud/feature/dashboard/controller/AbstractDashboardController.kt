package studio.crud.feature.dashboard.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import studio.crud.feature.core.web.annotations.CrudOperations
import studio.crud.feature.jpa.web.AbstractSimplifiedErrorHandlingJpaCrudController
import studio.crud.feature.core.web.model.ResultRO
import studio.crud.feature.dashboard.DashboardService
import studio.crud.feature.dashboard.model.DashboardDefinition
import studio.crud.feature.dashboard.ro.DashboardDefinitionRO

@CrudOperations(
    update = false,
    delete = false,
    create = false,
    index = true,
    show = true
)
abstract class AbstractDashboardController: AbstractSimplifiedErrorHandlingJpaCrudController<DashboardDefinition, DashboardDefinitionRO>() {
    @Autowired
    private lateinit var dashboardService: DashboardService

    @RequestMapping("/data")
    fun getDashboardData(@RequestParam widgetIds: List<Long>): ResponseEntity<ResultRO<*>> {
        return wrapResult { dashboardService.getDashboardData(widgetIds, accessorId(), accessorType()) }
    }

}