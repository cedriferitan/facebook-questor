package ro.tethys.facebook.questor.cron;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.tethys.facebook.questor.App;
import ro.tethys.facebook.questor.cron.job.SearchFacebookQuery;
import ro.tethys.facebook.questor.properties.AppProperties;

import static org.quartz.CronScheduleBuilder.cronSchedule;

public class JobScheduler {
    private static final Logger log = LoggerFactory.getLogger(JobScheduler.class);

    private final Scheduler scheduler;
    private final AppProperties properties = App.getProperties();

    public JobScheduler(SchedulerFactory schedulerFactory) throws SchedulerException {
        this.scheduler = schedulerFactory.getScheduler();
    }

    private void loadJob(Class<? extends Job> classJob, String cronSchedule) throws SchedulerException {
        scheduler.scheduleJob(buildJob(classJob), buildTrigger(cronSchedule));
    }

    private JobDetail buildJob(Class<? extends Job> job) {
        return JobBuilder.newJob(job).withIdentity(job.getName()).build();
    }

    private Trigger buildTrigger(String cronSchedule) {
        return TriggerBuilder
                .newTrigger()
                .withSchedule(cronSchedule(cronSchedule)).build();
    }

    public void runScheduler() throws SchedulerException {
        loadJob(SearchFacebookQuery.class, properties.cronExpression.trim());
        scheduler.start();
    }
}
