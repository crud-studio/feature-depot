package studio.crud.feature.auth.token.metadata

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.modelfilter.dsl.where
import studio.crud.feature.auth.token.metadata.model.TokenMetadata
import studio.crud.feature.auth.model.ParsedStatelessToken
import org.springframework.stereotype.Component
import studio.crud.feature.core.util.createOrUpdate

@Component
class TokenMetadataHandlerImpl(
    private val crudHandler: CrudHandler
) : TokenMetadataHandler {
    override fun updateTokenMetadata(parsedToken: ParsedStatelessToken, action: (tokenMetadata: TokenMetadata) -> Unit) {
        crudHandler.createOrUpdate<TokenMetadata>(
            where { "token" Equal parsedToken.token },
            action,
            { TokenMetadata(parsedToken.token, parsedToken.payload.entityUuid).apply(action) }

        )
            .execute()
    }
}