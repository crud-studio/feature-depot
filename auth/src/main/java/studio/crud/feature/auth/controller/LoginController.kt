package studio.crud.feature.auth.controller

import org.springframework.web.bind.annotation.*
import studio.crud.feature.auth.authentication.AuthenticationService
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.sharedcommon.utils.EMPTY_JSON_STRING
import studio.crud.sharedcommon.web.controller.BaseErrorHandlingController
import studio.crud.sharedcommon.web.ro.ResultRO
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("$API_PATH/login")
class LoginController(
    private val authenticationService: AuthenticationService
) : BaseErrorHandlingController() {
    @GetMapping("/methods")
    fun getAvailableMethodsForEntity(): ResultRO<*> {
        return wrapResult {
            return@wrapResult authenticationService.getAvailableMethods()
        }
    }

    @PostMapping("/{methodType}/initialize")
    fun initializeLogin(@PathVariable methodType: AuthenticationMethodType, @RequestBody(required = false) body: String = EMPTY_JSON_STRING, request: HttpServletRequest): ResultRO<*> {
        return wrapResult {
            return@wrapResult authenticationService.initializeLogin(methodType, body)
        }
    }

    @PostMapping("/{methodType}")
    fun doLogin(@PathVariable methodType: AuthenticationMethodType, @RequestBody(required = false) body: String = EMPTY_JSON_STRING, request: HttpServletRequest): ResultRO<*> {
        return wrapResult {
            val result = authenticationService.doLogin(methodType, body)
            return@wrapResult result
        }
    }
}

