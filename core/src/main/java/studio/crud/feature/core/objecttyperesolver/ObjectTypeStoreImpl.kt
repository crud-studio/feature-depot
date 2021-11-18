package studio.crud.feature.core.objecttyperesolver

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class ObjectTypeStoreImpl(
    @Autowired(required = false)
    private val resolutionProviders: List<ObjectTypeResolutionProvider>
) : ObjectTypeStore, InitializingBean {
    private lateinit var resolutions: Set<ObjectResolution>
    private lateinit var resolutionProvidersByNameCache: Map<String, ObjectResolution>
    private lateinit var resolutionProvidersByAliasCache: Map<String, ObjectResolution>
    private lateinit var resolutionProvidersByClazzCache: Map<KClass<*>, ObjectResolution>

    override fun afterPropertiesSet() {
        resolutions = resolutionProviders.flatMap { it.getResolutions() }.toSet()
        resolutionProvidersByNameCache = resolutions.associateBy { resolution -> resolution.name }
        resolutionProvidersByAliasCache = resolutions.associateBy { resolution -> resolution.alias }
        resolutionProvidersByClazzCache = resolutions.associateBy { resolution -> resolution.clazz }
    }

    override fun getName(alias: String): String {
        return resolutionProvidersByAliasCache[alias]!!.name
    }

    override fun getName(clazz: KClass<*>): String {
        return resolutionProvidersByClazzCache[clazz]!!.name
    }

    override fun getClazzByName(name: String): KClass<*> {
        return resolutionProvidersByNameCache[name]!!.clazz
    }
}