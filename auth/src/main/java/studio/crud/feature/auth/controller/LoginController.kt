package studio.crud.feature.auth.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import studio.crud.feature.auth.authentication.AuthenticationService
import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.feature.core.util.EMPTY_JSON_STRING
import studio.crud.sharedcommon.web.controller.AbstractErrorHandlingController
import studio.crud.sharedcommon.web.ro.ResultRO
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("$API_PATH/login")
class LoginController(
    private val authenticationService: AuthenticationService
) : AbstractErrorHandlingController() {
    @GetMapping("/methods")
    fun getAvailableMethodsForEntity(): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            return@wrapResult authenticationService.getAvailableMethods()
        }
    }

    @PostMapping("/{methodType}/initialize")
    fun initializeLogin(@PathVariable methodType: AuthenticationMethodType, @RequestBody(required = false) body: String = EMPTY_JSON_STRING, request: HttpServletRequest): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            return@wrapResult authenticationService.initializeLogin(methodType, body)
        }
    }

    @PostMapping("/{methodType}")
    fun doLogin(@PathVariable methodType: AuthenticationMethodType, @RequestBody(required = false) body: String = EMPTY_JSON_STRING, request: HttpServletRequest): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            val result = authenticationService.doLogin(methodType, body)
            return@wrapResult result
        }
    }
}

