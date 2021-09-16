package studio.crud.feature.quartz.model;

public abstract class AbstractQuartzJob implements Runnable {

	public int getJobIntervalSeconds() {
		return 0;
	}

	public String getJobCron() {
		return null;
	}

}

