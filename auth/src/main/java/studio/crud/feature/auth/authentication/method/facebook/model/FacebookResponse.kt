package studio.crud.feature.auth.authentication.method.facebook.model

data class FacebookResponse(
        val email: String? = null,
        val id: String = "",
        val error: FacebookErrorResponse? = null
)