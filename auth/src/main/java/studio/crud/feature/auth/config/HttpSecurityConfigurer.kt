package studio.crud.feature.auth.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
 * This interface is used instead of [WebSecurityConfigurerAdapter] in order to aggregate multiple configurations
 */
interface HttpSecurityConfigurer {
    fun configureHttp(http: HttpSecurity) {}
}