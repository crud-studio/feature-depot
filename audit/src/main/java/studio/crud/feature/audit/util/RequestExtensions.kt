package studio.crud.feature.audit.util

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import studio.crud.feature.audit.geoip.DefaultGeoIpResolver
import studio.crud.feature.audit.geoip.GeoIpResolver
import studio.crud.feature.audit.hostname.DefaultHostnameResolver
import studio.crud.feature.audit.hostname.HostnameResolver
import studio.crud.feature.core.util.getIpAddress
import javax.servlet.http.HttpServletRequest

var HttpServletRequest.requestId: String
    get() = this.getAttribute("requestId")?.toString() ?: "unknown"
    set(value) = this.setAttribute("requestId", value)

fun HttpServletRequest.getHostname(hostnameResolver: HostnameResolver = DefaultHostnameResolver): String = runBlocking {
    try {
        return@runBlocking withTimeout(1000) {
            hostnameResolver.resolve(getIpAddress())
        }
    } catch(e: TimeoutCancellationException) {
        return@runBlocking "timed.out"
    } catch(e: Exception) {
        return@runBlocking "unknown.host"
    }
}

fun HttpServletRequest.getCountryIso(geoIpResolver: GeoIpResolver = DefaultGeoIpResolver): String = runBlocking {
    try {
        return@runBlocking withTimeout(1000) {
            geoIpResolver.getCountryIso(getIpAddress())
        }
    } catch(e: TimeoutCancellationException) {
        return@runBlocking "TIMEOUT"
    } catch(e: Exception) {
        return@runBlocking "XX"
    }
}

fun HttpServletRequest.getFingerprint() = getHeader("x-fingerprint")
    ?: ""
