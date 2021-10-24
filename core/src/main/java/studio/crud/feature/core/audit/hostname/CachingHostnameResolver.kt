package studio.crud.feature.core.audit.hostname

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class CachingHostnameResolver(
    @Autowired(required = false)
    private val cacheManager: CacheManager?
): HostnameResolver, InitializingBean {
    private lateinit var hostnameResolutionCache: Cache

    override fun afterPropertiesSet() {
        hostnameResolutionCache = cacheManager?.getCache("hostnameResolutionCache") ?: hostnameResolutionCache
    }

    override fun resolve(ipAddress: String): String {
        val cached = hostnameResolutionCache.get(ipAddress)
        if(cached != null) {
            return cached as String
        }

        GlobalScope.async {
            val hostname = DefaultHostnameResolver.resolve(ipAddress)
            hostnameResolutionCache.put(ipAddress, hostname)
        }

        hostnameResolutionCache.put(ipAddress, "resolving")
        return ipAddress
    }
}