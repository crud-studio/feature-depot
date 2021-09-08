package studio.crud.feature.core.audit.geoip

object DefaultGeoIpResolver : GeoIpResolver {
    override fun getCountryIso(ip: String): String = GeoIpResolver.DEFAULT_COUNTRY_ISO
}