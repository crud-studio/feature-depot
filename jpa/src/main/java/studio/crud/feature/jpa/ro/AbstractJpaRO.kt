package studio.crud.feature.jpa.ro

import studio.crud.feature.jpa.model.AbstractJpaEntity
import java.io.Serializable

abstract class AbstractJpaRO : Serializable {
    var id: Long? = null

    final override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        other as AbstractJpaEntity?
        if (this === other) return true
        if (this.id == other.id) return true
        return true
    }

    final override fun hashCode(): Int {
        if (id == 0L) {
            return System.identityHashCode(this)
        }
        return id.hashCode()
    }
}