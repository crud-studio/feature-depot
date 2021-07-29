package studio.crud.feature.flyway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import studio.crud.feature.flyway.FlywayRunner

@Configuration
@ComponentScan("studio.crud.feature.flyway")
class FlywayFeatureAutoConfiguration {
    @Bean(initMethod = "migrate")
    fun flywayRunner() : FlywayRunner {
        return FlywayRunner()
    }
}