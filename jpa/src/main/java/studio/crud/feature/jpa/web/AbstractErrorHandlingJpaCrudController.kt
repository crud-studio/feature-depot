package studio.crud.feature.jpa.web

import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.crud.model.CRUDRequestBuilder
import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.modelfilter.dsl.where
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.GenericTypeResolver
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import studio.crud.feature.core.crud.enums.CrudOperationType
import studio.crud.feature.core.exception.OperationNotSupportedForEntityException
import studio.crud.feature.core.web.annotations.CrudOperations
import studio.crud.feature.core.web.controller.AbstractErrorHandlingController
import studio.crud.feature.core.web.hooks.GlobalCrudControllerHooks
import studio.crud.feature.core.web.model.ManyCrudResult
import studio.crud.feature.core.web.model.ManyFailedReason
import studio.crud.feature.core.web.model.ResultRO
import studio.crud.feature.jpa.model.AbstractJpaEntity
import studio.crud.feature.jpa.ro.AbstractJpaRO
import kotlin.reflect.KClass

// TODO: abstract beyond JPA

typealias AbstractSimplifiedErrorHandlingJpaCrudController<Entity, ReturnRO> = AbstractErrorHandlingJpaCrudController<Entity, ReturnRO, ReturnRO, ReturnRO, ReturnRO>

abstract class AbstractErrorHandlingJpaCrudController<Entity: AbstractJpaEntity, CreateRO: AbstractJpaRO, UpdateRO: AbstractJpaRO, ShowReturnRO: AbstractJpaRO, IndexReturnRO: AbstractJpaRO> : AbstractErrorHandlingController() {
    @Autowired
    private lateinit var crudHandler: CrudHandler

    @Autowired(required = false)
    private var globalHooks: Set<GlobalCrudControllerHooks> = emptySet()

    private val entityClazz: Class<Entity>

    private val showRoClazz: Class<ShowReturnRO>

    private val indexRoClazz: Class<IndexReturnRO>

    init {
        val generics = GenericTypeResolver.resolveTypeArguments(javaClass, AbstractErrorHandlingJpaCrudController::class.java)
        entityClazz = generics[0] as Class<Entity>
        showRoClazz = generics[3] as Class<ShowReturnRO>
        indexRoClazz = generics[4] as Class<IndexReturnRO>
    }

    @GetMapping("/{id}")
    @ResponseBody
    open fun show(@PathVariable id: Long): ResponseEntity<ResultRO<*>> {
        verifyOperation(CrudOperationType.Show)
        preShowInternal(id)
        return wrapResult {
            val builder = crudHandler.show(id, entityClazz, showRoClazz)
            builder
                .enforceIfNecessary()
                .execute()
        }
    }

    @PostMapping("/search")
    @ResponseBody
    open fun search(@RequestBody(required = false) filter: DynamicModelFilter? = null): ResponseEntity<ResultRO<*>> {
        verifyOperation(CrudOperationType.Index)
        val actualFilter = filter ?: where {}
        preSearchInternal(actualFilter)
        return wrapResult {
            val builder = crudHandler.index(actualFilter, entityClazz, indexRoClazz)
            builder
                .enforceIfNecessary()
                .execute()
        }
    }

    @PostMapping("/search/count")
    @ResponseBody
    open fun searchCount(@RequestBody(required = false) filter: DynamicModelFilter? = null): ResponseEntity<ResultRO<*>> {
        verifyOperation(CrudOperationType.Index)
        val actualFilter = filter ?: where {}
        preSearchInternal(actualFilter)
        return wrapResult {
            val builder = crudHandler.index(actualFilter, entityClazz)
            builder.enforceIfNecessary()
            builder.fromCache().count()
        }
    }

    @PostMapping
    @ResponseBody
    open fun create(@RequestBody ro: CreateRO): ResponseEntity<ResultRO<*>> {
        verifyOperation(CrudOperationType.Create)
        preCreateInternal(ro)
        return wrapResult {
            val builder = crudHandler.createFrom(ro, entityClazz, showRoClazz)
            builder
                .enforceIfNecessary()
                .execute()
        }
    }

    @PostMapping("/many")
    @ResponseBody
    open fun createMany(@RequestBody ros: List<CreateRO>): ResponseEntity<ResultRO<*>> {
        verifyOperation(CrudOperationType.Create)
        return wrapResult {
            val successful: MutableSet<ShowReturnRO> = mutableSetOf()
            val failed: MutableList<ManyFailedReason<CreateRO>> = mutableListOf()
            for (ro in ros) {
                preCreateInternal(ro)
                val builder = crudHandler.createFrom(ro, entityClazz, showRoClazz)
                builder.enforceIfNecessary()
                try {
                    successful.add(builder.execute() as ShowReturnRO)
                } catch (e: Exception) {
                    failed.add(ManyFailedReason(ro, e.message!!))
                }
            }
            ManyCrudResult(successful, failed)
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    open fun update(@PathVariable id: Long, @RequestBody ro: UpdateRO): ResponseEntity<ResultRO<*>> {
        verifyOperation(CrudOperationType.Update)
        preUpdateInternal(id, ro)
        return wrapResult {
            val builder = crudHandler.updateFrom(id, ro, entityClazz, showRoClazz)
            builder
                .enforceIfNecessary()
                .execute()
        }
    }

    @PutMapping("/many")
    @ResponseBody
    open fun updateMany(@RequestBody ros: List<UpdateRO>): ResponseEntity<ResultRO<*>> {
        verifyOperation(CrudOperationType.Update)
        return wrapResult {
            val successful: MutableSet<ShowReturnRO> = mutableSetOf()
            val failed: MutableList<ManyFailedReason<UpdateRO>> = mutableListOf()
            for (ro in ros) {
                preUpdateInternal(ro.id!!, ro)
                val builder = crudHandler.updateFrom(ro.id, ro, entityClazz, showRoClazz)
                builder.enforceIfNecessary()
                try {
                    successful.add(builder.execute() as ShowReturnRO)
                } catch (e: Exception) {
                    failed.add(ManyFailedReason(ro, e.message!!))
                }
            }
            ManyCrudResult(successful, failed)
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    open fun delete(@PathVariable id: Long): ResponseEntity<ResultRO<*>> {
        verifyOperation(CrudOperationType.Delete)
        preDeleteInternal(id)
        return wrapResult {
            val builder = crudHandler.delete(id, entityClazz)
            builder
                .enforceIfNecessary()
                .execute()
        }
    }

    @DeleteMapping("/many")
    @ResponseBody
    open fun deleteMany(@RequestBody ids: List<Long>): ResponseEntity<ResultRO<*>> {
        verifyOperation(CrudOperationType.Delete)
        return wrapResult {
            val successful: MutableSet<Long> = mutableSetOf()
            val failed: MutableList<ManyFailedReason<Long>> = mutableListOf()
            for (id in ids) {
                preDeleteInternal(id)
                val builder = crudHandler.delete(id, entityClazz)
                builder.enforceIfNecessary()
                try {
                    builder.execute()
                    successful.add(id)
                } catch (e: Exception) {
                    failed.add(ManyFailedReason(id, e.message!!))
                }
            }
            ManyCrudResult(successful, failed)
        }
    }

    open fun accessorType(): Class<*>? = null

    open fun accessorId(): Long? = null

    protected fun CRUDRequestBuilder<*, *, *, *>.enforceIfNecessary(): CRUDRequestBuilder<*, *, *, *> {
        if (accessorType() != null && accessorId() != null) {
            this.enforceAccess(accessorType(), accessorId())
        }
        return this
    }

    /**
     * Ran before a show operation
     */
    open fun preShow(id: Long) {}

    /**
     * Ran before a show operation
     */
    open fun preSearch(filter: DynamicModelFilter) {}

    /**
     * Ran before a create operation
     */
    open fun preCreate(ro: CreateRO) {}

    /**
     * Ran before an update operation
     */
    open fun preUpdate(id: Long, ro: UpdateRO) {}

    /**
     * Ran before a delete operation
     */
    open fun preDelete(id: Long) {}

    private fun preShowInternal(id: Long) {
        preShow(id)
        globalHooks.forEach { globalHook -> globalHook.preShow(
            id,
            this::class,
            entityClazz.kotlin as KClass<AbstractJpaEntity>
        ) }
    }

    private fun preSearchInternal(filter: DynamicModelFilter) {
        preSearch(filter)
        globalHooks.forEach { globalHook -> globalHook.preSearch(
            filter,
            this::class,
            entityClazz.kotlin as KClass<AbstractJpaEntity>
        ) }
    }

    private fun preCreateInternal(ro: CreateRO) {
        preCreate(ro)
        globalHooks.forEach { globalHook -> globalHook.preCreate(
            ro,
            this::class,
            entityClazz.kotlin as KClass<AbstractJpaEntity>
        ) }
    }

    private fun preUpdateInternal(id: Long, ro: UpdateRO) {
        preUpdate(id, ro)
        globalHooks.forEach { globalHook -> globalHook.preUpdate(
            id,
            ro,
            this::class,
            entityClazz.kotlin as KClass<AbstractJpaEntity>
        ) }
    }

    private fun preDeleteInternal(id: Long) {
        preDelete(id)
        globalHooks.forEach { globalHook -> globalHook.preDelete(
            id,
            this::class,
            entityClazz.kotlin as KClass<AbstractJpaEntity>
        ) }
    }

    private fun verifyOperation(crudOperationType: CrudOperationType) {
        val crudActions = javaClass.getDeclaredAnnotation(CrudOperations::class.java) ?: return
        when (crudOperationType) {
            CrudOperationType.Show -> if (!crudActions.show) {
                throw OperationNotSupportedForEntityException(crudOperationType, entityClazz.kotlin)
            }
            CrudOperationType.Index -> if (!crudActions.index) {
                throw OperationNotSupportedForEntityException(crudOperationType, entityClazz.kotlin)
            }
            CrudOperationType.Create -> if (!crudActions.create) {
                throw OperationNotSupportedForEntityException(crudOperationType, entityClazz.kotlin)
            }
            CrudOperationType.Update -> if (!crudActions.update) {
                throw OperationNotSupportedForEntityException(crudOperationType, entityClazz.kotlin)
            }
            CrudOperationType.Delete -> if (!crudActions.delete) {
                throw OperationNotSupportedForEntityException(crudOperationType, entityClazz.kotlin)
            }
        }
    }

}
