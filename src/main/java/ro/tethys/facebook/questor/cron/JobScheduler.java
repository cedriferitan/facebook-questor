package ro.tethys.facebook.questor.cron;

import org.quartz.*;
import ro.tethys.facebook.questor.App;
import ro.tethys.facebook.questor.cron.job.SearchFacebookQuery;
import ro.tethys.facebook.questor.properties.AppProperties;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.CronScheduleBuilder.cronSchedule;

public class JobScheduler {
    private final Scheduler scheduler;
    private final AppProperties properties = App.getProperties();
    private Map<Class<? extends Job>, JobKey> jobKeyMap = new HashMap<>();

    public JobScheduler(SchedulerFactory schedulerFactory) throws SchedulerException {
        this.scheduler = schedulerFactory.getScheduler();
    }

    private void loadJob(Class<? extends Job> classJob, String cronSchedule) throws SchedulerException {
        JobDetail jobDetail = buildJob(classJob);
        jobKeyMap.put(classJob, jobDetail.getKey());
        scheduler.scheduleJob(jobDetail, buildTrigger(cronSchedule));
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
//        scheduler.triggerJob(jobKeyMap.get(SearchFacebookQuery.class));
        scheduler.start();
    }
}
