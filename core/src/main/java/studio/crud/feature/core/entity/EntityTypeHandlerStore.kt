package studio.crud.feature.core.entity

import studio.crud.crudframework.model.BaseCrudEntity
import kotlin.reflect.KClass

interface EntityTypeHandlerStore {
    fun getDisplayRO(type: String, id: Long): EntityDisplayRO?
    fun getAvailableTypes(): Set<String>
    fun getTypeForClazz(clazz: KClass<*>): String
    fun getClazzForType(type: String): KClass<BaseCrudEntity<Long>>
}