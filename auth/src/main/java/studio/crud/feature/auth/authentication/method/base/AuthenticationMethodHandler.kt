package studio.crud.feature.auth.authentication.method.base

import studio.crud.crudframework.utils.component.componentmap.annotation.ComponentMapKey
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.auth.exception.AuthenticationMethodNotPasswordBasedException
import kotlin.reflect.KClass

interface AuthenticationMethodHandler<PayloadType : Any>  {
    @get:ComponentMapKey
    val methodType: AuthenticationMethodType

    val payloadType: KClass<PayloadType>

    fun getUsernameFromPayload(payload: PayloadType) : String

    fun initializeLogin(payload: PayloadType, method: EntityAuthenticationMethod): CustomParamsDTO = CustomParamsDTO()

    fun doLogin(payload: PayloadType, method: EntityAuthenticationMethod)

    fun initializeRegistration(payload: PayloadType): CustomParamsDTO = CustomParamsDTO()

    fun doRegister(payload: PayloadType, params: CustomParamsDTO, entity: Entity): EntityAuthenticationMethod

    fun getEntityMethod(payload: PayloadType): EntityAuthenticationMethod?

    fun changePassword(newPassword: String, method: EntityAuthenticationMethod) {
        throw AuthenticationMethodNotPasswordBasedException(methodType)
    }

    fun checkPassword(payload: PayloadType, method: EntityAuthenticationMethod): Boolean {
        throw AuthenticationMethodNotPasswordBasedException(methodType)
    }

    fun isPasswordExpired(method: EntityAuthenticationMethod): Boolean {
        throw AuthenticationMethodNotPasswordBasedException(methodType)
    }

    fun getUsername(method: EntityAuthenticationMethod): String
}