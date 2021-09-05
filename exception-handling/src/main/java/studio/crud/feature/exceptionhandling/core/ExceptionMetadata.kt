package studio.crud.feature.exceptionhandling.core

@Target(AnnotationTarget.CLASS)
annotation class ExceptionMetadata(vararg val params: ExceptionParam)

