package studio.crud.feature.core.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ComponentScan("studio.crud.feature.core")
class CoreFeatureAutoConfiguration