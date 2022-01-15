package studio.crud.feature.jpa.model

import studio.crud.crudframework.fieldmapper.annotation.MappedField
import org.hibernate.annotations.CreationTimestamp
import studio.crud.feature.jpa.ro.AbstractJpaUpdatableRO
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class AbstractJpaUpdatableEntity : AbstractJpaEntity() {
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column
    @MappedField(target = AbstractJpaUpdatableRO::class)
    var creationTime: Date = Date()

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @Version
    @MappedField(target = AbstractJpaUpdatableRO::class)
    var lastUpdateTime: Date = Date()
}