package studio.crud.feature.auth.authentication.model

import studio.crud.crudframework.crud.annotation.DeleteColumn
import studio.crud.crudframework.crud.annotation.Deleteable
import studio.crud.feature.auth.entity.model.EntityAuthenticationMethod
import studio.crud.feature.jpa.model.AbstractJpaUpdatableEntity
import java.util.*
import javax.persistence.*

@javax.persistence.Entity
@Table(name = "auth_forgot_password_token")
@Deleteable(softDelete = true)
class ForgotPasswordToken(
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "method_id")
    var method: EntityAuthenticationMethod,

    @Column
    var entityId: Long,

    @Column
    var token: String = UUID.randomUUID().toString(),

    @DeleteColumn
    @Column(name = "is_expired", columnDefinition = "BOOLEAN DEFAULT FALSE")
    var expired: Boolean = false,

    @Column
    var deviceHash: String? = null
) : AbstractJpaUpdatableEntity()