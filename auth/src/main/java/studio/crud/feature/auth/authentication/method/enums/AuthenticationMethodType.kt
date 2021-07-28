package studio.crud.feature.auth.authentication.method.enums

enum class UsernameType {
    Email, Telephone, Username
}

enum class AuthenticationMethodType(
        val registrationInitializationRequired: Boolean = false,
        val loginInitializationRequired: Boolean = false,
        val passwordBased: Boolean = false,
        val usernameType: UsernameType,

        /**
         * Whether or not this integration supports registration on login and login on registration
         */
        val canCrossActions:  Boolean = true
) {
    EmailPassword(
            passwordBased = true,
            usernameType = UsernameType.Email
    ),
    Nexmo(
            registrationInitializationRequired = true,
            loginInitializationRequired = true,
            usernameType = UsernameType.Telephone
    ), Google(
            usernameType = UsernameType.Email
    ), Facebook(
            usernameType = UsernameType.Email
    )
}