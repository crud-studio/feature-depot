package studio.crud.feature.core.audit.hostname

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import net.sf.ehcache.Cache
import net.sf.ehcache.CacheManager
import net.sf.ehcache.Element
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
@ConditionalOnBean(CacheManager::class)
class CachingHostnameResolver(
    private val cacheManager: CacheManager
) : HostnameResolver {
    private lateinit var hostnameResolutionCache: Cache

    @PostConstruct
    private fun initCache() {
        cacheManager.addCache(Cache("hostnameResolutionCache", 1200, false, false, 600, 600))
        hostnameResolutionCache = cacheManager.getCache("hostnameResolutionCache")
    }

    override fun resolve(ipAddress: String): String {
        var cached = hostnameResolutionCache.get(ipAddress)
        if(cached != null) {
            return cached.objectValue as String
        }

        GlobalScope.async {
            val hostname = DefaultHostnameResolver.resolve(ipAddress)
            hostnameResolutionCache.put(Element(ipAddress, hostname))
        }

        cached = Element(ipAddress, "resolving")
        hostnameResolutionCache.put(cached)
        return cached.objectValue as String
    }
}