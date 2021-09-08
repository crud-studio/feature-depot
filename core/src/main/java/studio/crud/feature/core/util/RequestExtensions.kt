package studio.crud.feature.core.util

import javax.servlet.http.HttpServletRequest

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