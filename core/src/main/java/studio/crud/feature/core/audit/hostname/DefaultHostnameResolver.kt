package studio.crud.feature.core.audit.hostname

import java.net.InetAddress

object DefaultHostnameResolver : HostnameResolver {
    override fun resolve(ipAddress: String): String {
        val ip = InetAddress.getByName(ipAddress)
        val hostname = ip.hostName
        if(ipAddress == hostname) {
            return "unresolved"
        }
        return ip.hostName
    }
}