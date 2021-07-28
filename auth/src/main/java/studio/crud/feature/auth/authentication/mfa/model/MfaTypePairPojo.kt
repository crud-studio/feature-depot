package studio.crud.feature.auth.authentication.mfa.model

import studio.crud.feature.auth.authentication.mfa.enums.MfaType

data class MfaTypePairPojo(val entityId: Long, val mfaType: MfaType)