package studio.crud.feature.auth.authentication.model

data class AuthenticationResultDTO(
    val entityUuid: String,
    val token: String
)