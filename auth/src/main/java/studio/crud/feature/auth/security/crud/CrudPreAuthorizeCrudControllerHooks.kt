package studio.crud.feature.auth.security.crud

import com.antelopesystem.crudframework.model.BaseCrudEntity
import com.antelopesystem.crudframework.modelfilter.DynamicModelFilter
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import studio.crud.feature.auth.exception.CrudActionNotAllowedException
import studio.crud.feature.auth.security.crud.annotations.PreAuthorizeCreate
import studio.crud.feature.auth.security.crud.annotations.PreAuthorizeDelete
import studio.crud.feature.auth.security.crud.annotations.PreAuthorizeRead
import studio.crud.feature.auth.security.crud.annotations.PreAuthorizeUpdate
import studio.crud.feature.auth.security.spring.SpringSecurityExpressionHandler
import studio.crud.feature.core.web.hooks.GlobalCrudControllerHooks
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * Handles pre-authorize annotations on CRUD operations
 */
@Component
class CrudPreAuthorizeCrudControllerHooks(
    private val expressionHandler: SpringSecurityExpressionHandler
) : GlobalCrudControllerHooks {
    override fun preShow(id: Long, controllerClazz: KClass<*>, entityClazz: KClass<out BaseCrudEntity<*>>) {
        validateSpringPreAuthorize(controllerClazz)
        validateExpression(
            controllerClazz.findAnnotation<PreAuthorizeRead>()?.value
        )
    }

    override fun preSearch(filter: DynamicModelFilter?, controllerClazz: KClass<*>, entityClazz: KClass<out BaseCrudEntity<*>>) {
        validateSpringPreAuthorize(controllerClazz)
        validateExpression(
            controllerClazz.findAnnotation<PreAuthorizeRead>()?.value
        )
    }


    override fun preCreate(ro: Any, controllerClazz: KClass<*>, entityClazz: KClass<out BaseCrudEntity<*>>) {
        validateSpringPreAuthorize(controllerClazz)
        validateExpression(
            controllerClazz.findAnnotation<PreAuthorizeCreate>()?.value
        )
    }

    override fun preUpdate(id: Long, ro: Any, controllerClazz: KClass<*>, entityClazz: KClass<out BaseCrudEntity<*>>) {
        validateSpringPreAuthorize(controllerClazz)
        validateExpression(
            controllerClazz.findAnnotation<PreAuthorizeUpdate>()?.value
        )
    }

    override fun preDelete(id: Long, controllerClazz: KClass<*>, entityClazz: KClass<out BaseCrudEntity<*>>) {
        validateSpringPreAuthorize(controllerClazz)
        validateExpression(
            controllerClazz.findAnnotation<PreAuthorizeDelete>()?.value
        )
    }

    private fun validateSpringPreAuthorize(controllerClazz: KClass<*>) {
        val preAuthorize = controllerClazz.findAnnotation<PreAuthorize>()
        validateExpression(preAuthorize?.value)
    }

    private fun validateExpression(expression: String?) {
        if(expression == null) {
            return
        }
        val result = expressionHandler.check(expression)
        if(!result) {
            throw CrudActionNotAllowedException()
        }
    }
}