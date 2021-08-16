package studio.crud.feature.dashboard.ro

import java.io.Serializable

data class WidthDTO(
        val screenSize: String,
        val columnNumber: Int
): Serializable