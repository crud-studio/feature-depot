package studio.crud.feature.dashboard.widget

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class WidgetHelperImpl(
    private val widgetHelperDao: WidgetHelperDao
) : WidgetHelper {
    @Transactional(readOnly = true)
    override fun runNativeQuery(query: String): List<Any> {
        // todo: parameter layer
        return widgetHelperDao.runNativeQuery(query)
    }
}