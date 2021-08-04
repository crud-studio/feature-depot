package studio.crud.feature.auth.security.crud.annotations

import org.intellij.lang.annotations.Language

@Target(AnnotationTarget.CLASS)
annotation class PreAuthorizeUpdate(@Language("SpEL") val value: String)