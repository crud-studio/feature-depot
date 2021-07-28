package studio.crud.feature.auth.authentication.method.google.model

import javax.validation.constraints.NotEmpty

class GoogleAuthenticationPayloadDTO(
    @field:NotEmpty
    val idToken: String?
)