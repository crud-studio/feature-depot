package studio.crud.feature.auth.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.firewall.DefaultHttpFirewall
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import studio.crud.feature.auth.RestAuthenticationEntryPoint
import studio.crud.feature.auth.controller.secure.SECURE_API_PATH
import studio.crud.feature.auth.filter.AuthenticationFilter
import studio.crud.feature.auth.provider.TokenAuthenticationProvider

@Configuration
class AuthSpringConfig : WebSecurityConfigurerAdapter(), WebMvcConfigurer {

    @Autowired
    private lateinit var authenticationFilter: AuthenticationFilter

    @Autowired
    private lateinit var restAuthenticationEntryPoint: RestAuthenticationEntryPoint

    @Autowired
    private lateinit var tokenAuthenticationProvider: TokenAuthenticationProvider

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedOrigins("*")
            .allowedHeaders("x-token", "x-client-public-key", "Content-Type")
            .exposedHeaders("x-server-public-key")
            .maxAge(3600)
            .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
    }

    @Bean(name = [BeanIds.AUTHENTICATION_MANAGER, "authenticationManager"])
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(tokenAuthenticationProvider)
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .sessionManagement().disable()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .and()
            .authorizeRequests()
            .antMatchers("$SECURE_API_PATH/**").authenticated()
            .and()
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .cors()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun defaultHttpFirewall(): HttpFirewall = DefaultHttpFirewall()

    @Bean
    fun authenticationFilterRegistration(): FilterRegistrationBean<AuthenticationFilter> {
        val registration = FilterRegistrationBean<AuthenticationFilter>()
        registration.filter = authenticationFilter
        registration.isEnabled = false
        return registration
    }


}