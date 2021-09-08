package studio.crud.feature.core.audit.geoip

import com.maxmind.geoip2.DatabaseReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import mu.KotlinLogging
import net.sf.ehcache.Cache
import net.sf.ehcache.CacheManager
import net.sf.ehcache.Element
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import studio.crud.feature.core.audit.config.MaxmindProperties
import java.net.InetAddress

@ConditionalOnProperty("${MaxmindProperties.PREFIX}.enabled", havingValue = "true")
@Component
class CachingMaxmindGeoIpResolver(
    private val maxmindProperties: MaxmindProperties,
    private val cacheManager: CacheManager
) : GeoIpResolver, InitializingBean {
    private lateinit var databaseReader: DatabaseReader

    private lateinit var geoIpResolutionCache: Cache

    override fun afterPropertiesSet() {
        val resource: Resource = ClassPathResource(maxmindProperties.dbPath)
        val inputStream = resource.inputStream
        databaseReader = DatabaseReader.Builder(inputStream).build()

        cacheManager.addCache(Cache("geoIpResolutionCache", 1200, false, false, 600, 600))
        geoIpResolutionCache = cacheManager.getCache("geoIpResolutionCache")
    }

    override fun getCountryIso(ip: String): String {
        var cached = geoIpResolutionCache.get(ip)
        if(cached != null) {
            return cached.objectValue as String
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
            geoIpResolutionCache.put(Element(ip, countryIso))
        }

        cached = Element(ip, "resolving")
        geoIpResolutionCache.put(cached)
        return cached.objectValue as String
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}