package studio.crud.feature.auth.model

import studio.crud.feature.auth.trait.annotations.AuthenticationTrait

data class UserInfo(
    val internalId: Long,
    val entityUuid: String,
    val parsedToken: ParsedStatelessToken,
    val traits: List<AuthenticationTrait>,
    val grants: Set<String>
) {
    companion object {
        fun UserInfo.hasGrant(grant: String): Boolean {
            return hasAnyGrant(grant)
        }
        fun UserInfo.hasAnyGrant(vararg grants: String): Boolean {
            return this.grants.any { it in grants }
        }
    }
}