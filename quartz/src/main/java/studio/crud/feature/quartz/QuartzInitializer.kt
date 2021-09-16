package studio.crud.feature.quartz

import com.antelopesystem.crudframework.utils.component.startup.annotation.PostStartUp
import mu.KotlinLogging
import org.quartz.Scheduler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import studio.crud.feature.quartz.model.AbstractQuartzJob

@Component
class QuartzInitializer(
    @Autowired(required = false)
    private val quartzJobs: Map<String, AbstractQuartzJob> = emptyMap(),
    private var scheduler: Scheduler
) {
    @PostStartUp
    fun init() {
        try {
            QuartzJobUtil.setupQuartzJobs(quartzJobs, scheduler)
            scheduler.start()
        } catch (e: Exception) {
            log.error(e) { "Could not initialize quartz "}
            throw e
        }
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}