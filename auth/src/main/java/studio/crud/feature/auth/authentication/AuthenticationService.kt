package studio.crud.feature.auth.authentication

import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.auth.authentication.model.AuthenticationResultDTO
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.model.UserInfo

interface AuthenticationService {
    fun initializeLogin(methodType: AuthenticationMethodType, body: String): CustomParamsDTO
    fun doLogin(methodType: AuthenticationMethodType, body: String): AuthenticationResultDTO
    fun initializeRegistration(methodType: AuthenticationMethodType, body: String): CustomParamsDTO
    fun doRegister(methodType: AuthenticationMethodType, body: String): AuthenticationResultDTO
    fun initializeForgotPassword(methodType: AuthenticationMethodType, body: String)
    fun redeemForgotPasswordToken(tokenString: String, newPassword: String)
    fun changePassword(methodType: AuthenticationMethodType, body: String, newPassword: String, userInfo: UserInfo): String
    fun getAvailableMethods(): List<AuthenticationServiceImpl.Companion.AuthenticationMethodDTO>
}