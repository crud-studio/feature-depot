package studio.crud.feature.core.objecttyperesolver

import kotlin.reflect.KClass

interface ObjectTypeStore {
    fun getName(alias: String): String
    fun getName(clazz: KClass<*>): String
    fun getClazzByName(name: String): KClass<*>
}
