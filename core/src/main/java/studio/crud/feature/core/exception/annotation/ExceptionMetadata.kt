package studio.crud.feature.core.exception.annotation

@Target(AnnotationTarget.CLASS)
annotation class ExceptionMetadata(vararg val params: ExceptionParam)

