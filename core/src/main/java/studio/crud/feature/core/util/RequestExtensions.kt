package studio.crud.feature.core.util

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import studio.crud.feature.core.audit.geoip.DefaultGeoIpResolver
import studio.crud.feature.core.audit.geoip.GeoIpResolver
import studio.crud.feature.core.audit.hostname.DefaultHostnameResolver
import studio.crud.feature.core.audit.hostname.HostnameResolver
import javax.servlet.http.HttpServletRequest

var HttpServletRequest.requestId: String
    get() = this.getAttribute("requestId")?.toString() ?: "unknown"
    set(value) = this.setAttribute("requestId", value)

fun HttpServletRequest.getUserAgent() = getHeader("User-Agent")
    ?: ""

fun HttpServletRequest.getXForwardedForValue() = getHeader("x-forwarded-for")
    ?: getHeader("X_FORWARDED_FOR")
    ?: ""

fun HttpServletRequest.getIpAddress(): String {
    try {
        var ipAddress: String? = null
        val xForwardedForHeaderValue: String = getXForwardedForValue()
        if (xForwardedForHeaderValue.isNotBlank()) {
            try {
                val xForwardedForArray = xForwardedForHeaderValue.split(",").toTypedArray()
                ipAddress = xForwardedForArray[xForwardedForArray.size - 1].trim()
            } catch (e: Exception) {
            }
        }
        if (ipAddress == null) {
            ipAddress = remoteAddr!!
        }

        return ipAddress
    } catch (e: Exception) {
        return ""
    }
}

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
