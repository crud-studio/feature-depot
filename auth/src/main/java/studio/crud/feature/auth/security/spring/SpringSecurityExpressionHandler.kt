package studio.crud.feature.auth.security.spring

import mu.KotlinLogging
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.security.access.expression.ExpressionUtils
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.util.SimpleMethodInvocation
import org.springframework.stereotype.Component
import java.lang.reflect.Method
import kotlin.reflect.jvm.javaMethod

@Component
class SpringSecurityExpressionHandler {
    private var triggerCheckMethod: Method = SecurityObject::triggerCheck.javaMethod!!
    private var parser: SpelExpressionParser = SpelExpressionParser()
    private val expressionHandler: MethodSecurityExpressionHandler = DefaultMethodSecurityExpressionHandler()

    fun check(securityExpression: String): Boolean {
        log.debug {"Checking security expression [ $securityExpression] " }
        val securityObject = SecurityObject()
        val evaluationContext = expressionHandler.createEvaluationContext(SecurityContextHolder.getContext().authentication, SimpleMethodInvocation(securityObject, triggerCheckMethod))
        val checkResult = ExpressionUtils.evaluateAsBoolean(parser.parseExpression(securityExpression), evaluationContext)
        log.debug {"Check result: $checkResult" }
        return checkResult
    }

    private class SecurityObject {
        fun triggerCheck() {}
    }
    companion object {
        private val log = KotlinLogging.logger { }
    }
}