package studio.crud.feature.core.web.annotations

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class CrudOperations(
    val index: Boolean = true,
    val show: Boolean = true,
    val update: Boolean = true,
    val create: Boolean = true,
    val delete: Boolean = true
)