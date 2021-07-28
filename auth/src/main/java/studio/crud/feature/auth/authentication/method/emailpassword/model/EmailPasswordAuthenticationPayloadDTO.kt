package studio.crud.feature.auth.authentication.method.emailpassword.model

import studio.crud.feature.auth.authentication.validation.LoginValidation
import studio.crud.feature.auth.authentication.validation.RegistrationValidation
import javax.validation.constraints.NotEmpty

class EmailPasswordAuthenticationPayloadDTO(
    @field:NotEmpty
    val email: String?,

    @field:NotEmpty(groups = [LoginValidation::class, RegistrationValidation::class])
    val password: String?
)