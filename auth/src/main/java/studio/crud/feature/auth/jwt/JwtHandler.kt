package studio.crud.feature.auth.jwt

import studio.crud.feature.auth.model.ParsedStatelessToken
import studio.crud.feature.auth.model.TokenPayload

interface JwtHandler {
    fun generateToken(payload: TokenPayload): ParsedStatelessToken
    fun verifyAndDecodeToken(token: String): ParsedStatelessToken
}