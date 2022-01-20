package studio.crud.feature.core.entity

import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.utils.component.componentmap.annotation.ComponentMapKey
import kotlin.reflect.KClass

interface EntityTypeHandler<T : BaseCrudEntity<Long>> {
    /**
     * The entity type identifier
     */
    @get:ComponentMapKey
    val type: String

    /**
     * The class of the entity
     */
    val entityClazz: KClass<T>

    /**
     * Get a display row given [entity]
     */
    fun getDisplayRO(entity: T): EntityDisplayRO
}