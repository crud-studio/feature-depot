package studio.crud.feature.audit

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import studio.crud.feature.audit.geoip.DefaultGeoIpResolver
import studio.crud.feature.audit.geoip.GeoIpResolver
import studio.crud.feature.audit.hostname.DefaultHostnameResolver
import studio.crud.feature.audit.hostname.HostnameResolver
import javax.servlet.http.HttpServletRequest

@Component
class RequestSecurityMetadataProviderImpl(
    private val request: HttpServletRequest,
    @Autowired(required = false)
        private val hostnameResolver: HostnameResolver?,
    @Autowired(required = false)
        private val geoIpResolver: GeoIpResolver?
) : RequestSecurityMetadataProvider {

    override fun get(): RequestSecurityMetadata {
        try {
            request.method // Call to trigger an exception if the request is not present
            return RequestSecurityMetadata.from(request, hostnameResolver ?: DefaultHostnameResolver, geoIpResolver ?: DefaultGeoIpResolver)
        } catch(e: IllegalStateException) {
            return RequestSecurityMetadata.EMPTY
        }
    }
}