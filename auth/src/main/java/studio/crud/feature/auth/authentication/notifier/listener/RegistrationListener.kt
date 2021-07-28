package studio.crud.feature.auth.authentication.notifier.listener

import studio.crud.feature.auth.authentication.model.MethodRequestPayload
import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.auth.model.ParsedStatelessToken
import studio.crud.sharedcommon.audit.RequestSecurityMetadata

interface RegistrationListener : AuthenticationListener{
    fun onRegistrationSuccess(body: String, method: EntityAuthenticationMethod, parsedStatelessToken: ParsedStatelessToken, securityMetadata: RequestSecurityMetadata) {}
    fun onRegistrationFailure(body: String, securityMetadata: RequestSecurityMetadata, error: String) {}
}

