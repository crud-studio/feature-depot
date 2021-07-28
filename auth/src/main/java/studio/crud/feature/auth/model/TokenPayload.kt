package studio.crud.feature.auth.model

import java.util.*

data class TokenPayload(
    val entityUuid: String,
    val mfaRequired: Boolean,
    val passwordChangeRequired: Boolean,
    val expiryDate: Date
)