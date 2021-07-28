package studio.crud.feature.auth.trait

import studio.crud.feature.auth.trait.annotations.AuthenticationTrait
import studio.crud.feature.auth.trait.annotations.AuthenticationTraits
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import javax.servlet.http.HttpServletRequest

@Component
class RequestAuthenticationTraitResolverImpl(
    private val mappings: RequestMappingHandlerMapping
) : RequestAuthenticationTraitResolver {
    override fun resolve(request: HttpServletRequest): List<AuthenticationTrait> {
        val result = mutableListOf<AuthenticationTrait>()
        val handler = getRequestHandler(request) ?: return emptyList()
        val authenticationTraitAnnotations = AnnotationUtils.getAnnotations(handler.method)?.mapNotNull { AnnotationUtils.findAnnotation(it::class.java, AuthenticationTrait::class.java) }
        if(authenticationTraitAnnotations != null) {
            result.addAll(authenticationTraitAnnotations)
        }

        val authenticationTraitsAnnotations = AnnotationUtils.getAnnotations(handler.method)?.mapNotNull { AnnotationUtils.findAnnotation(it::class.java, AuthenticationTraits::class.java) }

        if(authenticationTraitsAnnotations != null) {
            result.addAll(authenticationTraitsAnnotations.flatMap { it.traits.toList() })
        }

        return result
    }

    private fun getRequestHandler(request: HttpServletRequest): HandlerMethod? {
        val handlerExecutionChain = mappings.getHandler(request)
        if (handlerExecutionChain != null) {
            return handlerExecutionChain.handler as HandlerMethod
        }
        return null
    }
}