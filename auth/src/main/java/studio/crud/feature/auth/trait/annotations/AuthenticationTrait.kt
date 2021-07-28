package studio.crud.feature.auth.trait.annotations

annotation class AuthenticationTrait(val value: String) {
    companion object {
        fun List<AuthenticationTrait>.hasTrait(value: String): Boolean {
            return this.any { it.value == value }
        }
    }
}