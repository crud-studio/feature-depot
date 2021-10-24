package studio.crud.feature.core.audit.geoip

import com.maxmind.geoip2.DatabaseReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.support.NoOpCache
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import studio.crud.feature.core.audit.config.MaxmindProperties
import java.net.InetAddress

@ConditionalOnProperty("${MaxmindProperties.PREFIX}.enabled", havingValue = "true")
@Component
class CachingMaxmindGeoIpResolver(
    private val maxmindProperties: MaxmindProperties,
    @Autowired(required = false)
    private val cacheManager: CacheManager?
) : GeoIpResolver, InitializingBean {
    private lateinit var databaseReader: DatabaseReader

    private var geoIpResolutionCache: Cache = NoOpCache("geoIpResolutionCache")

    override fun afterPropertiesSet() {
        val resource: Resource = ClassPathResource(maxmindProperties.dbPath)
        val inputStream = resource.inputStream
        databaseReader = DatabaseReader.Builder(inputStream).build()
        geoIpResolutionCache = cacheManager?.getCache("geoIpResolutionCache") ?: geoIpResolutionCache
    }

    override fun getCountryIso(ip: String): String {
        val cached = geoIpResolutionCache.get(ip)
        if(cached != null) {
            return cached as String
        }

        GlobalScope.async {
            val countryIso = try {
                val ipAddress = InetAddress.getByName(ip)
                val response = databaseReader.country(ipAddress)
                response.country.isoCode ?: DefaultGeoIpResolver.getCountryIso(ip)
            } catch(e: Exception) {
                log.error(e) { "Failed to resolve IP [ $ip ] to countryIso" }
                DefaultGeoIpResolver.getCountryIso(ip)
            }
            geoIpResolutionCache.put(ip, countryIso)
        }

        geoIpResolutionCache.put(ip, "resolving")
        return ip
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}