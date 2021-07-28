package studio.crud.feature.auth.trait

import studio.crud.feature.auth.trait.annotations.AuthenticationTrait
import javax.servlet.http.HttpServletRequest

interface RequestAuthenticationTraitResolver {
    fun resolve(request: HttpServletRequest): List<AuthenticationTrait>
}