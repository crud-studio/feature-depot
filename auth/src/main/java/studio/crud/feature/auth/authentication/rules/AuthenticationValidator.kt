package studio.crud.feature.auth.authentication.rules

import studio.crud.feature.auth.entity.model.Entity
import studio.crud.sharedcommon.audit.RequestSecurityMetadata

interface AuthenticationValidator {
    fun validate(entity: Entity, securityMetadata: RequestSecurityMetadata): Int
}