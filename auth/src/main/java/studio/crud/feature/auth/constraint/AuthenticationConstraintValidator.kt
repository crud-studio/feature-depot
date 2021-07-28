package studio.crud.feature.auth.constraint

import studio.crud.feature.auth.model.UserInfo

interface AuthenticationConstraintValidator {
    fun traitsToIgnore(): List<String> = emptyList()
    fun validate(userInfo: UserInfo)
}