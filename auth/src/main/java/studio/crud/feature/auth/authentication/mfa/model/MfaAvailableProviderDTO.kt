package studio.crud.feature.auth.authentication.mfa.model

import studio.crud.feature.auth.authentication.mfa.enums.MfaType

data class MfaAvailableProviderDTO(val mfaType: MfaType, val enabled: Boolean)