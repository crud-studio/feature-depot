package studio.crud.feature.auth.model

data class ParsedStatelessToken(
    val token: String,
    val payload: TokenPayload
)