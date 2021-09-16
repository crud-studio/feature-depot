package studio.crud.feature.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import studio.crud.feature.quartz.model.AbstractQuartzJob;
import studio.crud.feature.quartz.model.QuartzJobClass;

import java.util.Date;
import java.util.Map;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by Shani on 05/12/2018.
 */
public class QuartzJobUtil {

	public static void setupQuartzJobs(Map<String, AbstractQuartzJob> quartzMap, Scheduler scheduler) {
		for(Map.Entry<String, AbstractQuartzJob> entry : quartzMap.entrySet()) {
			setupFlushJob(entry.getKey(), entry.getValue(), scheduler);
		}
	}

	private static void setupFlushJob(String name, AbstractQuartzJob job, Scheduler scheduler) {
		Log log = LogFactory.getLog(job.getClass());

		JobDetail jobDetail = newJob(QuartzJobClass.class).withIdentity("quartzJob-" + name, "quartzJobs").build();
		jobDetail.getJobDataMap().put("jobHandler", job);

		ScheduleBuilder schedule = getSchedule(job);

		if(schedule == null) {
			return;
		}

		Trigger trigger = newTrigger()
				.withIdentity("quartzTrig-" + name, "quartzJobs")
				.startAt(new Date(System.currentTimeMillis() + (60L * 1000L)))
				.withSchedule(schedule)
				.build();
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error(e, e);
		}
	}

	private static ScheduleBuilder getSchedule(AbstractQuartzJob job) {
		if(job.getJobCron() != null && !job.getJobCron().isEmpty()) {
			return cronSchedule(job.getJobCron());
		} else if(job.getJobIntervalSeconds() > 0) {
			return simpleSchedule().repeatForever().withIntervalInSeconds(job.getJobIntervalSeconds());
		}

		return null;
	}
}


