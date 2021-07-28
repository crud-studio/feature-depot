package studio.crud.feature.auth.provider

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import studio.crud.feature.auth.constraint.AuthenticationConstraintValidator
import studio.crud.feature.auth.exception.InvalidTokenException
import studio.crud.feature.auth.exception.MfaRequiredException
import studio.crud.feature.auth.exception.PasswordChangeRequiredException
import studio.crud.feature.auth.grant.GrantProvider
import studio.crud.feature.auth.jwt.JwtHandler
import studio.crud.feature.auth.model.ParsedStatelessToken
import studio.crud.feature.auth.model.TokenAuthentication
import studio.crud.feature.auth.model.TokenAuthenticationRequest
import studio.crud.feature.auth.model.UserInfo
import studio.crud.feature.auth.resolvers.EntityInternalIdResolver
import studio.crud.feature.auth.trait.CommonTraits
import studio.crud.feature.auth.trait.annotations.AuthenticationTrait.Companion.hasTrait

@Component
class TokenAuthenticationProvider(
    @Autowired(required=false) private val constraintValidators: List<AuthenticationConstraintValidator> = emptyList(),
    private val jwtHandler: JwtHandler,
    private val entityInternalIdResolver: EntityInternalIdResolver,
    @Autowired(required=false) private val grantProviders: List<GrantProvider>?
) : AuthenticationProvider {
    override fun supports(authentication: Class<*>): Boolean {
        return TokenAuthenticationRequest::class.java.isAssignableFrom(authentication)
    }

    override fun authenticate(authentication: Authentication): Authentication {
        authentication as TokenAuthenticationRequest
        val parsedToken = parseToken(authentication.token)

        if(parsedToken.payload.mfaRequired && !authentication.traits.hasTrait(CommonTraits.IGNORE_MFA)) {
            throw MfaRequiredException()
        }

        if(parsedToken.payload.passwordChangeRequired && !authentication.traits.hasTrait(CommonTraits.IGNORE_PASSWORD_EXPIRED)) {
            throw PasswordChangeRequiredException()
        }

        val internalId = entityInternalIdResolver.resolve(parsedToken.payload.entityUuid)

        val grants = mutableSetOf<String>()
        if(!grantProviders.isNullOrEmpty()) {
            for (grantProvider in grantProviders) {
                grants += grantProvider.provide(internalId)
            }
        }
        val userInfo = UserInfo(
            internalId,
            parsedToken.payload.entityUuid,
            parsedToken,
            authentication.traits,
            grants
        )

        for (constraintValidator in constraintValidators) {
            val traitsToIgnore = constraintValidator.traitsToIgnore()
            if(traitsToIgnore.any { userInfo.traits.hasTrait(it) }) {
                continue
            }
            constraintValidator.validate(userInfo)
        }
        
        return TokenAuthentication(userInfo)
    }

    private fun parseToken(token: String): ParsedStatelessToken {
        // todo: sort errors
        try {
            val parsedToken = jwtHandler.verifyAndDecodeToken(token)
            return parsedToken
        } catch(e: Exception) {
            throw InvalidTokenException()
        }
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}
