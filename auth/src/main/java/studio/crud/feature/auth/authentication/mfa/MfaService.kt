package studio.crud.feature.auth.authentication.mfa

import studio.crud.feature.auth.authentication.mfa.enums.MfaType
import studio.crud.feature.auth.authentication.mfa.model.MfaAvailableProviderDTO
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.authentication.model.MethodRequestPayload
import studio.crud.feature.auth.model.UserInfo

interface MfaService {
    fun setup(mfaType: MfaType, body: String, userInfo: UserInfo): CustomParamsDTO
    fun activate(mfaType: MfaType, code: String, userInfo: UserInfo)
    fun deactivate(mfaType: MfaType, userInfo: UserInfo)
    fun issue(mfaType: MfaType, userInfo: UserInfo)
    fun validateCurrentToken(mfaType: MfaType, code: String, userInfo: UserInfo): String
    fun getAvailableProviders(userInfo: UserInfo): List<MfaAvailableProviderDTO>
    fun getEnabledProviders(userInfo: UserInfo): List<MfaType>
}