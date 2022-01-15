package studio.crud.feature.core.util

import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.crud.hooks.update.CRUDOnUpdateHook
import studio.crud.crudframework.crud.hooks.update.CRUDPostUpdateHook
import studio.crud.crudframework.crud.hooks.update.CRUDPreUpdateHook
import studio.crud.crudframework.crud.model.CRUDRequestBuilder
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.modelfilter.FilterField
import studio.crud.crudframework.modelfilter.dsl.where
import studio.crud.crudframework.modelfilter.enums.FilterFieldOperation

inline fun <reified EntityType : BaseCrudEntity<Long>> CrudHandler.updateById(id: Long, crossinline callback: (entity: EntityType) -> Unit): CRUDRequestBuilder<CRUDPreUpdateHook<Long, EntityType>, CRUDOnUpdateHook<Long, EntityType>, CRUDPostUpdateHook<Long, EntityType>, MutableList<EntityType>> {
    return this.updateByFilter(where { "id" Equal id }, EntityType::class.java)
            .withOnHook(CRUDOnUpdateHook {
                callback(it)
            })
}

inline fun <reified EntityType : BaseCrudEntity<Long>, reified RoType> CrudHandler.updateByIdToRo(id: Long, crossinline callback: (entity: EntityType) -> Unit): CRUDRequestBuilder<CRUDPreUpdateHook<Long, EntityType>, CRUDOnUpdateHook<Long, EntityType>, CRUDPostUpdateHook<Long, EntityType>, MutableList<RoType>> {
    return this.updateByFilter(where { "id" Equal id }, EntityType::class.java, RoType::class.java)
            .withOnHook(CRUDOnUpdateHook {
                callback(it)
            })
}

fun <EntityType : BaseCrudEntity<Long>> CRUDRequestBuilder<CRUDPreUpdateHook<Long, EntityType>, CRUDOnUpdateHook<Long, EntityType>, CRUDPostUpdateHook<Long, EntityType>, MutableList<EntityType>>.executeSingle(): EntityType {
    return execute().first()
}

/**
 * Attempt to run an update operation using the given [filter], if found, apply [onUpdate] to the object. If not found, retrieve the object from [onCreate] and create it
 */
inline fun <reified EntityType : BaseCrudEntity<Long>> CrudHandler.createOrUpdate(filter: DynamicModelFilter, crossinline onUpdate: (EntityType) -> Unit, crossinline onCreate: () -> EntityType): ExecuteWrapper<EntityType> {
    return ExecuteWrapper {
        this.updateByFilter(filter, EntityType::class.java)
            .withOnHook(CRUDOnUpdateHook {
                onUpdate(it)
            })
            .execute().firstOrNull() ?: this.create(onCreate()).execute()
    }

}

class ExecuteWrapper<ResultType>(private val function: () -> ResultType) {
    fun execute() : ResultType {
        try {
            return function()
        } catch(e: Exception) {
            e.printStackTrace()
            throw e;
        }

    }
}

fun DynamicModelFilter.getField(name: String, operation: FilterFieldOperation): FilterField? {
    return this.filterFields.find { it.fieldName == name && it.operation == operation }
}


fun <T> DynamicModelFilter.getFieldValue(name: String, operation: FilterFieldOperation, clazz: Class<T>): T? {
    val field = this.getField(name, operation) ?: return null
    return field.value1 as T?
}

fun <EntityType : BaseCrudEntity<Long>> CRUDRequestBuilder<CRUDPreUpdateHook<Long, EntityType>, CRUDOnUpdateHook<Long, EntityType>, CRUDPostUpdateHook<Long, EntityType>, MutableList<EntityType>>.executeSingleOrNull(): EntityType? {
    return execute().firstOrNull()
}


