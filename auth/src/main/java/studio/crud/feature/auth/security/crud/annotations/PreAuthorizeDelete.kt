package studio.crud.feature.auth.security.crud.annotations

import org.intellij.lang.annotations.Language

@Target(AnnotationTarget.CLASS)
annotation class PreAuthorizeDelete(@Language("SpEL") val value: String)