package studio.crud.feature.auth.annotations

import studio.crud.feature.auth.trait.CommonTraits
import studio.crud.feature.auth.trait.annotations.AuthenticationTrait

@AuthenticationTrait(CommonTraits.IGNORE_PASSWORD_EXPIRED)
annotation class IgnorePasswordExpired