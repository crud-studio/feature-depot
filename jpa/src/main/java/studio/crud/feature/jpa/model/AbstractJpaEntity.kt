package studio.crud.feature.jpa.model

import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField
import com.antelopesystem.crudframework.jpa.annotation.JpaCrudEntity
import com.antelopesystem.crudframework.model.BaseCrudEntity
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.TypeDef
import studio.crud.feature.jpa.ro.AbstractJpaRO
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
@JpaCrudEntity
@TypeDef(
    name = "json",
    typeClass = JsonStringType::class
)
abstract class AbstractJpaEntity : BaseCrudEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @MappedField(target = AbstractJpaRO::class)
    final override var id: Long = 0L

    final override fun exists(): Boolean = id != null && id != 0L

    final override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        other as AbstractJpaEntity?
        if (this === other) return true
        if (this.id == other.id) return true
        return false
    }

    final override fun hashCode(): Int {
        if (id == 0L) {
            return System.identityHashCode(this)
        }
        return id.hashCode()
    }
}