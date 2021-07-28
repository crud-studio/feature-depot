package studio.crud.feature.auth.token.metadata

import studio.crud.feature.auth.model.ParsedStatelessToken
import studio.crud.feature.auth.token.metadata.model.TokenMetadata

interface TokenMetadataHandler {
    fun updateTokenMetadata(parsedToken: ParsedStatelessToken, action: (tokenMetadata: TokenMetadata) -> Unit)
}