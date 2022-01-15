package studio.crud.feature.auth.authentication.mfa.provider.base

import studio.crud.crudframework.utils.component.componentmap.annotation.ComponentMapKey
import studio.crud.feature.auth.authentication.mfa.enums.MfaType
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.exception.MfaIssueNotSupportedException
import studio.crud.feature.auth.exception.MfaValidateNotSupportedException
import kotlin.reflect.KClass

interface MfaProvider<PayloadType : Any> {
    @get:ComponentMapKey
    val type: MfaType

    val payloadType: KClass<PayloadType>

    fun setup(payload: PayloadType, entity: Entity): CustomParamsDTO

    fun issue(entity: Entity, params: CustomParamsDTO) {
        throw MfaIssueNotSupportedException(type)
    }

    fun validate(code: String, entity: Entity, params: CustomParamsDTO) {
        throw MfaValidateNotSupportedException(type)
    }
}