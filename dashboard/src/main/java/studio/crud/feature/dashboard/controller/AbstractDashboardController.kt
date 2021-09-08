package studio.crud.feature.dashboard.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import studio.crud.feature.dashboard.DashboardService
import studio.crud.feature.dashboard.model.DashboardDefinition
import studio.crud.feature.dashboard.ro.DashboardDefinitionRO
import studio.crud.sharedcommon.crud.web.annotations.CrudOperations
import studio.crud.sharedcommon.web.controller.AbstractSimplifiedErrorHandlingJpaCrudController
import studio.crud.feature.core.web.model.ResultRO

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