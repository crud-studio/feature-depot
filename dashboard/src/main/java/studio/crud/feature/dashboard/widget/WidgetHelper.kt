package studio.crud.feature.dashboard.widget

import org.springframework.transaction.annotation.Transactional

/**
 * A helper bean for widgets
 */
interface WidgetHelper {
    @Transactional(readOnly = true)
    fun runNativeQuery(query: String): List<Any>
}