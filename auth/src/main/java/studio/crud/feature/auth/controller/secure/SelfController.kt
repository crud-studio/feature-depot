package studio.crud.feature.auth.controller.secure

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import studio.crud.feature.auth.annotations.IgnorePasswordExpired
import studio.crud.feature.auth.authentication.AuthenticationService
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.auth.model.UserInfo
import studio.crud.sharedcommon.utils.EMPTY_JSON_STRING
import studio.crud.sharedcommon.web.controller.BaseErrorHandlingController
import studio.crud.sharedcommon.web.ro.ResultRO
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("$SECURE_API_PATH/self")
class SelfController(
    private val authenticationService: AuthenticationService
) : BaseErrorHandlingController() {
    @PostMapping("/{methodType}/changePassword")
    @IgnorePasswordExpired
    fun changePassword(@PathVariable methodType: AuthenticationMethodType, @RequestBody(required = false) body: String = EMPTY_JSON_STRING, @RequestParam newPassword: String, request: HttpServletRequest, @AuthenticationPrincipal userInfo: UserInfo?): ResultRO<*> {
        return wrapResult {
            return@wrapResult authenticationService.changePassword(methodType, body, newPassword, userInfo!!)
        }
    }
}