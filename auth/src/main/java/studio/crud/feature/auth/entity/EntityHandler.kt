package studio.crud.feature.auth.entity

import studio.crud.feature.auth.entity.model.Entity

interface EntityHandler {
    fun getEntityById(entityId: Long): Entity
    fun getEntityUsername(entityId: Long): String
    fun getEntityUsername(entity: Entity): String
}