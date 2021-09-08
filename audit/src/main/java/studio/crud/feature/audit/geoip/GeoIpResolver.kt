package studio.crud.feature.audit.geoip

interface GeoIpResolver {
    fun getCountryIso(ip: String): String
    companion object {
        const val DEFAULT_COUNTRY_ISO = "XX"
    }
}