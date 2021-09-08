package studio.crud.feature.audit.geoip

object DefaultGeoIpResolver : GeoIpResolver {
    override fun getCountryIso(ip: String): String = GeoIpResolver.DEFAULT_COUNTRY_ISO
}