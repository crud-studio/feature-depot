package studio.crud.feature.auth.token

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import studio.crud.feature.auth.jwt.JwtHandler
import studio.crud.feature.auth.model.ParsedStatelessToken
import studio.crud.feature.auth.model.TokenPayload

@Component
class TokenHandlerImpl : TokenHandler {
    @Autowired
    private lateinit var jwtHandler: JwtHandler

    override fun generateToken(payload: TokenPayload): ParsedStatelessToken {
        return jwtHandler.generateToken(payload)
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}