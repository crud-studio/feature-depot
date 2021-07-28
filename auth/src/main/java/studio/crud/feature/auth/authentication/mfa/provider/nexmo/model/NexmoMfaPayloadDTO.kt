package studio.crud.feature.auth.authentication.mfa.provider.nexmo.model

import javax.validation.constraints.NotEmpty

class NexmoMfaPayloadDTO(
    @field:NotEmpty
    val telephonePrefix: String?,

    @field:NotEmpty
    val telephone: String?
)