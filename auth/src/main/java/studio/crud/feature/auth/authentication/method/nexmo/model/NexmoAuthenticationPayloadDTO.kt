package studio.crud.feature.auth.authentication.method.nexmo.model

import studio.crud.feature.auth.authentication.validation.LoginValidation
import studio.crud.feature.auth.authentication.validation.RegistrationValidation
import javax.validation.constraints.NotEmpty

class NexmoAuthenticationPayloadDTO(
    @field:NotEmpty
    val telephonePrefix: String?,

    @field:NotEmpty
    val telephone: String?,

    @field:NotEmpty(groups = [RegistrationValidation::class, LoginValidation::class])
    val code: String?
)