package studio.crud.feature.audit

interface RequestSecurityMetadataProvider {
    fun get(): RequestSecurityMetadata
}