package studio.crud.feature.auth.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import studio.crud.feature.auth.authentication.AuthenticationService
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.core.util.EMPTY_JSON_STRING
import studio.crud.feature.core.web.controller.AbstractErrorHandlingController
import studio.crud.feature.core.web.model.ResultRO
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("$API_PATH/forgotPassword")
class ForgotPasswordController(
    private val authenticationService: AuthenticationService
) : AbstractErrorHandlingController() {
    @PostMapping("/{methodType}")
    fun initializeForgotPassword(@PathVariable methodType: AuthenticationMethodType, @RequestBody(required = false) body: String = EMPTY_JSON_STRING, request: HttpServletRequest): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            authenticationService.initializeForgotPassword(methodType, body)
        }
    }

    @PostMapping("/redeem/{token}")
    fun redeemForgotPasswordToken(@PathVariable token: String, @RequestParam newPassword: String): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            authenticationService.redeemForgotPasswordToken(token, newPassword)
        }
    }
}

