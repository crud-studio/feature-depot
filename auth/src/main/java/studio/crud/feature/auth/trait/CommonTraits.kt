package studio.crud.feature.auth.trait

object CommonTraits {
    /**
     * Bypasses the MFA check
     */
    const val IGNORE_MFA = "ignoreMfa"

    /**
     * Bypasses the password expired check
     */
    const val IGNORE_PASSWORD_EXPIRED = "ignorePasswordExpired"
}