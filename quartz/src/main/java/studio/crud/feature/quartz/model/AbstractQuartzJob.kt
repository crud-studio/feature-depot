package studio.crud.feature.quartz.model

import java.time.Duration

/**
 * Represents a quartz job bean
 *
 */
abstract class AbstractQuartzJob : Runnable {
    /**
     * The job duration, highest precedence if set
     */
    open val jobDuration: Duration? = null

    /**
     * Job cron, takes precedence over job interval in seconds if set
     */
    open val jobCron: String? = null

    /**
     * Job interval in seconds, lowest precedence
     */
    open val jobIntervalSeconds: Int? = null
}