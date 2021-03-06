package studio.crud.feature.auth.authentication.rules.action.base

import studio.crud.crudframework.utils.component.componentmap.annotation.ComponentMapKey
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.authentication.rules.RuleActionType
import studio.crud.feature.core.audit.RequestSecurityMetadata

interface RuleActionHandler {

    @get:ComponentMapKey
    val actionType: RuleActionType

    fun handle(entity: Entity, securityMetadata: RequestSecurityMetadata)
}