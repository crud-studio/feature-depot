package studio.crud.feature.quartz.model

import org.quartz.Job
import org.quartz.JobExecutionContext

class QuartzJobClass : Job {
    override fun execute(context: JobExecutionContext) {
        val jobHandler = context.jobDetail.jobDataMap["jobHandler"] as AbstractQuartzJob?
        jobHandler!!.run()
    }
}