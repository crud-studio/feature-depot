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
@RequestMapping("$API_PATH/register")
class RegistrationController(
    private val authenticationService: AuthenticationService
) : AbstractErrorHandlingController() {

    @PostMapping("/{methodType}/initialize")
    fun initializeRegistration(@PathVariable methodType: AuthenticationMethodType, @RequestBody(required = false) body: String = EMPTY_JSON_STRING, request: HttpServletRequest): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            return@wrapResult authenticationService.initializeRegistration(methodType, body)
        }
    }

    @PostMapping("/{methodType}")
    fun doRegister(@PathVariable methodType: AuthenticationMethodType, @RequestBody(required = false) body: String = EMPTY_JSON_STRING, request: HttpServletRequest): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            val result = authenticationService.doRegister(methodType, body)
            return@wrapResult result
        }
    }
}

