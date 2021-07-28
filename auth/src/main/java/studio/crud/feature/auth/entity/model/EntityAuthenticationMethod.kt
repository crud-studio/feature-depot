package studio.crud.feature.auth.entity.model

import studio.crud.feature.auth.authentication.method.enums.AuthenticationMethodType
import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaUpdatableEntity
import javax.persistence.*

@javax.persistence.Entity
@Table(name = "auth_entity_authentication_method")
class EntityAuthenticationMethod(
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entity_id")
    var entity: Entity,

    @Column
    @Enumerated(EnumType.STRING)
    var methodType: AuthenticationMethodType,

    @Column
    var param1: String? = null,

    @Column
    var param2: String? = null,

    @Column
    var param3: String? = null,

    @Column
    var param4: String? = null,

    @Column
    var param5: String? = null,

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    var active: Boolean = true,

    @Column(name = "is_primary", columnDefinition = "BOOLEAN DEFAULT FALSE")
    var primary: Boolean = false
) : AbstractJpaUpdatableEntity()

