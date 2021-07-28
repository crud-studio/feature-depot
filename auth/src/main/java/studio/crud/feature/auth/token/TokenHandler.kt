package studio.crud.feature.auth.token

import studio.crud.feature.auth.model.ParsedStatelessToken
import studio.crud.feature.auth.model.TokenPayload

interface TokenHandler {
    fun generateToken(payload: TokenPayload): ParsedStatelessToken
}
