package studio.crud.feature.jpa.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("studio.crud.feature.jpa")
@EntityScan("studio.crud.feature")
class JpaFeatureAutoConfiguration