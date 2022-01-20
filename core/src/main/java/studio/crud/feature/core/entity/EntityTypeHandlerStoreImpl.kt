package studio.crud.feature.core.entity

import org.springframework.stereotype.Component
import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.utils.component.componentmap.annotation.ComponentMap
import studio.crud.feature.core.entity.exception.EntityTypeHandlerNotFoundException
import studio.crud.feature.core.entity.exception.EntityTypeHandlerNotFoundForClassException
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

@Component
class EntityTypeHandlerStoreImpl(
    private val crudHandler: CrudHandler
) : EntityTypeHandlerStore {
    @ComponentMap
    private lateinit var entityTypeHandlers: Map<String, EntityTypeHandler<BaseCrudEntity<Long>>>

    override fun getAvailableTypes(): Set<String> {
        return entityTypeHandlers.keys.toSet()
    }

    override fun getTypeForClazz(clazz: KClass<*>): String {
        val result = entityTypeHandlers.values
            .find { handler -> handler.entityClazz == clazz } ?: throw EntityTypeHandlerNotFoundForClassException(clazz.jvmName)
        return result.type
    }

    override fun getClazzForType(type: String): KClass<BaseCrudEntity<Long>> {
        val handler = getEntityTypeHandler(type)
        return handler.entityClazz
    }

    override fun getDisplayRO(type: String, id: Long): EntityDisplayRO? {
        val typeHandler = getEntityTypeHandler(type)
        val entity = crudHandler.show(id, typeHandler.entityClazz.java)
            .execute() ?: return null
        return typeHandler.getDisplayRO(entity)
    }

    private fun getEntityTypeHandler(type: String): EntityTypeHandler<BaseCrudEntity<Long>> {
        return entityTypeHandlers[type] ?: throw EntityTypeHandlerNotFoundException(type)
    }
}