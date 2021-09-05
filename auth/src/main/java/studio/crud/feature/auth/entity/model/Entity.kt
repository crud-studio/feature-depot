package studio.crud.feature.auth.entity.model

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import studio.crud.feature.jpa.model.AbstractJpaUpdatableEntity
import java.util.*
import javax.persistence.*
import javax.persistence.Entity

@Entity
@Table(name = "auth_entity")
class Entity(
    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, targetEntity = EntityAuthenticationMethod::class, mappedBy = "entity", orphanRemoval = true, cascade = [CascadeType.ALL])
    var authenticationMethods: MutableList<EntityAuthenticationMethod> = mutableListOf(),

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, targetEntity = EntityMfaMethod::class, mappedBy = "entity", orphanRemoval = true, cascade = [CascadeType.ALL])
    var mfaMethods: MutableList<EntityMfaMethod> = mutableListOf(),

    @Column
    var email: String? = null,

    @Column(updatable = false)
    var uuid: String = UUID.randomUUID().toString(),

    @Column
    var telephone: String? = null
) : AbstractJpaUpdatableEntity()