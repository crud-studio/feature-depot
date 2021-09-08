package studio.crud.feature.audit.hostname

interface HostnameResolver {
    fun resolve(ipAddress: String): String
}