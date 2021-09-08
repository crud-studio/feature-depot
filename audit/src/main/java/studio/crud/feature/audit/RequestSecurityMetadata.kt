package studio.crud.feature.audit

import studio.crud.feature.audit.geoip.GeoIpResolver
import studio.crud.feature.audit.hostname.HostnameResolver
import studio.crud.feature.audit.util.getCountryIso
import studio.crud.feature.audit.util.getFingerprint
import studio.crud.feature.audit.util.getHostname
import studio.crud.feature.audit.util.requestId
import studio.crud.feature.core.util.getIpAddress
import studio.crud.feature.core.util.getUserAgent
import studio.crud.feature.core.util.getXForwardedForValue
import java.io.Serializable
import java.util.*
import javax.servlet.http.HttpServletRequest

data class RequestSecurityMetadata(
        val requestId: String = "",
        val ip: String = "",
        val ipChain: String = "",
        val hostname: String = "",
        val countryIso: String = "",
        val userAgent: String = "",
        val fingerprint: String = "",
        val requestUri: String = "",
        val requestMethod: String = "",
        val headers: Map<String, String> = mapOf()
) : Serializable {
    companion object {
        private val requestCache = WeakHashMap<String, RequestSecurityMetadata>()
        val EMPTY = RequestSecurityMetadata()

        fun from(request: HttpServletRequest, hostnameResolver: HostnameResolver, geoIpResolver: GeoIpResolver): RequestSecurityMetadata {
            var requestUuid = request.getAttribute("request.uuid") as String?
            if (requestUuid == null) {
                requestUuid = UUID.randomUUID().toString()
                request.setAttribute("request.uuid", requestUuid)
            }
            return requestCache.computeIfAbsent(requestUuid) {
                var ip = request.getIpAddress()
                var ipChain = request.getXForwardedForValue()
                if (ipChain.isBlank()) {
                    ipChain = ip
                }
                val headers = request.headerNames
                        .toList()
                        .map { it to request.getHeader(it) }
                        .toMap()
                return@computeIfAbsent RequestSecurityMetadata(
                        request.requestId,
                        ip,
                        ipChain,
                        request.getHostname(hostnameResolver),
                        request.getCountryIso(geoIpResolver),
                        request.getUserAgent(),
                        request.getFingerprint(),
                        request.requestURI,
                        request.method,
                        headers
                )
            }
        }
    }
}