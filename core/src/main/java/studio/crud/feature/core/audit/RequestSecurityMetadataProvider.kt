package studio.crud.feature.core.audit

interface RequestSecurityMetadataProvider {
    fun get(): RequestSecurityMetadata
}