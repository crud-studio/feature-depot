package studio.crud.feature.dashboard.widget

interface WidgetHelperDao {
    fun runNativeQuery(queryString: String): List<Any>
}