package studio.crud.feature.dashboard.exception

import studio.crud.feature.core.exception.ServerException
import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.annotation.ExceptionParam
import studio.crud.feature.dashboard.enums.DashboardWidgetType

@ExceptionMetadata(
    params = [
        ExceptionParam("dashboardWidgetType")
    ]
)
class NoHandlerFoundForDashboardWidgetTypeException(val dashboardWidgetType: DashboardWidgetType) : ServerException("No handler found for widget type [ $dashboardWidgetType ] ")