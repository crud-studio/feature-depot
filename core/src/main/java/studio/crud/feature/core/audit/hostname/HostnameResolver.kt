package studio.crud.feature.core.audit.hostname

interface HostnameResolver {
    fun resolve(ipAddress: String): String
}