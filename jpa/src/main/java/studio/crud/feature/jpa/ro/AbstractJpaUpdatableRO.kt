package studio.crud.feature.jpa.ro

import java.util.*

abstract class AbstractJpaUpdatableRO : AbstractJpaRO() {
    var creationTime: Date? = null
    var lastUpdateTime: Date? = null
}