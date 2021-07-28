package studio.crud.feature.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.stereotype.Component
import studio.crud.feature.auth.config.properties.AuthProperties
import studio.crud.feature.auth.model.ParsedStatelessToken
import studio.crud.feature.auth.model.TokenPayload

@Component
class JwtHandlerImpl(
    private val authProperties: AuthProperties
) : JwtHandler {
    override fun generateToken(payload: TokenPayload): ParsedStatelessToken {
        val token = JWT.create()
            .withIssuer("todo")
            .withExpiresAt(payload.expiryDate)
            .withClaim("entityUuid", payload.entityUuid)
            .withClaim("mfaRequired", payload.mfaRequired)
            .withClaim("passwordChangeRequired", payload.passwordChangeRequired)
            .sign(createAlgorithm())
        return ParsedStatelessToken(token, payload);
    }
    
    override fun verifyAndDecodeToken(token: String): ParsedStatelessToken {
        val decodedJWT = createVerifier().verify(token)

        return ParsedStatelessToken(
            token,
            TokenPayload(
                decodedJWT.getClaim("entityUuid").asString(),
                decodedJWT.getClaim("mfaRequired").asBoolean(),
                decodedJWT.getClaim("passwordChangeRequired").asBoolean(),
                decodedJWT.expiresAt
            )
        )
    }

    private fun createVerifier(): JWTVerifier {
        return JWT.require(createAlgorithm())
            .withIssuer("todo")
            .build()
    }

    private fun createAlgorithm(): Algorithm {
        return Algorithm.HMAC256(authProperties.secret)
    }
}