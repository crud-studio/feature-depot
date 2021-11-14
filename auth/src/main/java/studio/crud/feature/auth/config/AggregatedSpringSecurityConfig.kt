package studio.crud.feature.auth.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class AggregatedSpringSecurityConfig(
    @Autowired(required = false)
    private val httpSecurityConfigurers: List<HttpSecurityConfigurer>
) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        for (httpSecurityConfig in httpSecurityConfigurers) {
            httpSecurityConfig.configureHttp(http)
        }
    }
}

