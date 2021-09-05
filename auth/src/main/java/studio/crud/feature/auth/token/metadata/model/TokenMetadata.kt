package studio.crud.feature.auth.token.metadata.model

import studio.crud.feature.jpa.model.AbstractJpaUpdatableEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "auth_token_metadata")
class TokenMetadata(
    @Column(columnDefinition = "TEXT")
    var token: String,

    @Column
    var entityUuid: String,

    @Column(nullable = true)
    var deviceHash: String? = null,

    @Column(nullable = true)
    var deviceScore: Int? = null
) : AbstractJpaUpdatableEntity()
