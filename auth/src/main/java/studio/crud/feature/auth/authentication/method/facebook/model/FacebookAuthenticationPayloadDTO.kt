package studio.crud.feature.auth.authentication.method.facebook.model

import javax.validation.constraints.NotEmpty

class FacebookAuthenticationPayloadDTO(
    @field:NotEmpty
    val accessToken: String?
)