package studio.crud.feature.auth.authentication.rules

import com.antelopesystem.crudframework.crud.annotation.Deleteable
import studio.crud.feature.jpa.model.AbstractJpaUpdatableEntity
import javax.persistence.*

@Entity
@Table(name = "auth_authentication_rule")
@Deleteable(softDelete = false)
class AuthenticationRule(
        @Column(name = "min_score", columnDefinition = "INT DEFAULT 0")
        var minScore: Int = 0,

        @Enumerated(EnumType.STRING)
        @Column(name = "action")
        var action: RuleActionType? = null
) : AbstractJpaUpdatableEntity()