package studio.crud.feature.dashboard.widget.type.core

import org.springframework.core.GenericTypeResolver
import studio.crud.feature.core.util.extractAndValidatePayload
import studio.crud.feature.dashboard.model.DashboardWidget
import kotlin.reflect.KClass

abstract class AbstractDashboardWidgetTypeDataHandler<ResultType : Any, SettingsType : Any> : DashboardWidgetTypeDataHandler<ResultType> {
    val resultClazz: KClass<ResultType>
    val settingsClazz: KClass<SettingsType>

    init {
        val generics = GenericTypeResolver.resolveTypeArguments(this::class.java, AbstractDashboardWidgetTypeDataHandler::class.java)
        resultClazz = (generics[0] as Class<ResultType>).kotlin
        settingsClazz = (generics[1] as Class<SettingsType>).kotlin
    }

    final override fun getData(widget: DashboardWidget): ResultType {
        val settings = extractAndValidatePayload(widget.settings, settingsClazz)
        return getData(widget, settings)
    }

    abstract fun getData(widget: DashboardWidget, settings: SettingsType): ResultType
}