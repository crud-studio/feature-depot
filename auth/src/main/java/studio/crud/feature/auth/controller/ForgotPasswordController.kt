package studio.crud.feature.auth.controller

import org.springframework.web.bind.annotation.*
import studio.crud.feature.auth.authentication.AuthenticationService
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.sharedcommon.utils.EMPTY_JSON_STRING
import studio.crud.sharedcommon.web.controller.BaseErrorHandlingController
import studio.crud.sharedcommon.web.ro.ResultRO
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("$API_PATH/forgotPassword")
class ForgotPasswordController(
    private val authenticationService: AuthenticationService
) : BaseErrorHandlingController() {
    @PostMapping("/{methodType}")
    fun initializeForgotPassword(@PathVariable methodType: AuthenticationMethodType, @RequestBody(required = false) body: String = EMPTY_JSON_STRING, request: HttpServletRequest): ResultRO<*> {
        return wrapVoidResult {
            authenticationService.initializeForgotPassword(methodType, body)
        }
    }

    @PostMapping("/redeem/{token}")
    fun redeemForgotPasswordToken(@PathVariable token: String, @RequestParam newPassword: String): ResultRO<*> {
        return wrapVoidResult {
            authenticationService.redeemForgotPasswordToken(token, newPassword)
        }
    }
}

