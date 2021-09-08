package studio.crud.feature.auth.controller.secure

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import studio.crud.feature.auth.annotations.IgnoreMfa
import studio.crud.feature.auth.authentication.mfa.MfaService
import studio.crud.feature.auth.authentication.mfa.enums.MfaType
import studio.crud.feature.auth.model.UserInfo
import studio.crud.feature.core.util.EMPTY_JSON_STRING
import studio.crud.feature.core.web.controller.AbstractErrorHandlingController
import studio.crud.feature.core.web.model.ResultRO
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("$SECURE_API_PATH/mfa")
class MfaController(
    private val mfaService: MfaService
): AbstractErrorHandlingController() {

    @PostMapping("/{mfaType}/setup")
    fun setup(@PathVariable mfaType: MfaType, @AuthenticationPrincipal userInfo: UserInfo, request: HttpServletRequest, @RequestBody(required = false) body: String = EMPTY_JSON_STRING) : ResponseEntity<ResultRO<*>> {
        return wrapResult {
            return@wrapResult mfaService.setup(mfaType, body, userInfo)
        }
    }

    @PostMapping("/{mfaType}/activate")
    fun activate(@PathVariable mfaType: MfaType, @RequestParam code: String, @AuthenticationPrincipal userInfo: UserInfo) : ResponseEntity<ResultRO<*>> {
        return wrapResult {
            mfaService.activate(mfaType, code, userInfo)
        }
    }

    @PostMapping("/{mfaType}/deactivate")
    fun deactivate(@PathVariable mfaType: MfaType, @AuthenticationPrincipal userInfo: UserInfo) : ResponseEntity<ResultRO<*>> {
        return wrapResult {
            mfaService.deactivate(mfaType, userInfo)
        }
    }

    @IgnoreMfa
    @PostMapping("/{mfaType}/issue")
    fun issue(@PathVariable mfaType: MfaType, @AuthenticationPrincipal userInfo: UserInfo) : ResponseEntity<ResultRO<*>> {
        return wrapResult {
            mfaService.issue(mfaType, userInfo)
        }
    }

    @IgnoreMfa
    @PostMapping("/{mfaType}/validate-token")
    fun validateCurrentToken(@PathVariable mfaType: MfaType, @RequestParam code: String, @AuthenticationPrincipal userInfo: UserInfo) : ResponseEntity<ResultRO<*>> {
        return wrapResult {
            mfaService.validateCurrentToken(mfaType, code, userInfo)
        }
    }

    @GetMapping("/providers")
    fun getAvailableProvidersForUser(@AuthenticationPrincipal userInfo: UserInfo): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            return@wrapResult mfaService.getAvailableProviders(userInfo)
        }
    }

    @GetMapping("/providers/enabled")
    @IgnoreMfa
    fun getEnabledProvidersForUser(@AuthenticationPrincipal userInfo: UserInfo): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            return@wrapResult mfaService.getEnabledProviders(userInfo)
        }
    }
}