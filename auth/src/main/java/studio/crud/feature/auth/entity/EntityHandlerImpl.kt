package studio.crud.feature.auth.entity

import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.modelfilter.dsl.where
import studio.crud.crudframework.utils.component.componentmap.annotation.ComponentMap
import org.springframework.stereotype.Component
import studio.crud.feature.auth.authentication.method.base.AuthenticationMethodHandler
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.exception.EntityNotFoundByIdException

@Component
class EntityHandlerImpl(
        private val crudHandler: CrudHandler
) : EntityHandler {
    @ComponentMap
    private lateinit var authenticationMethodTypeHandlers: Map<AuthenticationMethodType, AuthenticationMethodHandler<Any>>

    override fun getEntityById(entityId: Long): Entity {
        return crudHandler.showBy(where {
            "id" Equal entityId
        }, Entity::class.java).execute() ?: throw EntityNotFoundByIdException(entityId)
    }

    override fun getEntityUsername(entityId: Long): String {
        return getEntityUsername(getEntityById(entityId))
    }

    override fun getEntityUsername(entity: Entity): String {
        val primaryMethod = entity.authenticationMethods.find { it.primary } ?: return UNKNOWN_USERNAME
        val handler = authenticationMethodTypeHandlers[primaryMethod.methodType] ?: return UNKNOWN_USERNAME
        return handler.getUsername(primaryMethod)
    }

    companion object {
        private const val UNKNOWN_USERNAME = "unknown"
    }
}