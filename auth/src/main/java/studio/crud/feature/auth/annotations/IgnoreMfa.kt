package studio.crud.feature.auth.annotations

import studio.crud.feature.auth.trait.CommonTraits
import studio.crud.feature.auth.trait.annotations.AuthenticationTrait

/**
 * Use on controller methods in order to bypass MFA on them
 */
@AuthenticationTrait(CommonTraits.IGNORE_MFA)
annotation class IgnoreMfa