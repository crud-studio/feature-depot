package studio.crud.feature.core.web.hooks

import com.antelopesystem.crudframework.model.BaseCrudEntity
import com.antelopesystem.crudframework.modelfilter.DynamicModelFilter
import kotlin.reflect.KClass


/**
 * Beans of this type will run hooks on all subclasses of [AbstractErrorHandlingJpaCrudController]
 */
interface GlobalCrudControllerHooks {
    /**
     * Ran before a show operation
     */
    fun preShow(id: Long, controllerClazz: KClass<*>, entityClazz: KClass<out BaseCrudEntity<*>>) {}

    /**
     * Ran before a show operation
     */
    fun preSearch(filter: DynamicModelFilter?, controllerClazz: KClass<*>, entityClazz: KClass<out BaseCrudEntity<*>>) {}

    /**
     * Ran before a create operation
     */
    fun preCreate(ro: Any, controllerClazz: KClass<*>, entityClazz: KClass<out BaseCrudEntity<*>>) {}

    /**
     * Ran before an update operation
     */
    fun preUpdate(id: Long, ro: Any, controllerClazz: KClass<*>, entityClazz: KClass<out BaseCrudEntity<*>>) {}

    /**
     * Ran before a delete operation
     */
    fun preDelete(id: Long, controllerClazz: KClass<*>, entityClazz: KClass<out BaseCrudEntity<*>>) {}
}