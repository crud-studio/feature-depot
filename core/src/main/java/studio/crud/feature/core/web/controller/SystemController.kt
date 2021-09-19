package studio.crud.feature.core.web.controller

import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.info.GitProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import studio.crud.feature.core.util.closestTagName
import studio.crud.feature.core.web.model.InstanceInfoDTO

@RestController
@RequestMapping("/system")
class SystemController(
    @Autowired(required = false)
    private val buildProperties: BuildProperties? = null,

    @Autowired(required = false)
    private val gitProperties: GitProperties? = null
): AbstractErrorHandlingController(), InitializingBean {
    override fun afterPropertiesSet() {
        log.info { "System Controller is active" }
    }

    @GetMapping("/status")
    fun getSystemStatus() = wrapResult {  }

    @GetMapping("/time")
    fun getSystemTime() = wrapResult { System.currentTimeMillis() }

    @GetMapping("/build")
    fun getBuildInfo() = wrapResult {
        if(buildProperties == null || gitProperties == null) {
            return@wrapResult InstanceInfoDTO()
        }
        return@wrapResult InstanceInfoDTO(buildProperties.time, System.getenv("HOSTNAME"), gitProperties.closestTagName)
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}

