package studio.crud.feature.core.crud.enums

enum class CrudOperationType {
    Show, Index, Create, Update, Delete;

    companion object {
        val VALUES = values()
    }
}