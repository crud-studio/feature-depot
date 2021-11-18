package studio.crud.feature.core.objecttyperesolver

import kotlin.reflect.KClass

interface ObjectTypeResolutionProvider {
    fun getResolutions(): Set<ObjectResolution>
}

data class ObjectResolution(val name: String, val alias: String, val clazz: KClass<*>)