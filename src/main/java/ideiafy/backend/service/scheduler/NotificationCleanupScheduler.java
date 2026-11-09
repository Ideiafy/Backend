package ideiafy.backend.service.scheduler;

import ideiafy.backend.service.scheduler.jobs.NotificationCleanupJob;
import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationCleanupScheduler {

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void scheduleJob() {

        try {

            JobDetail jobDetail = JobBuilder.newJob(NotificationCleanupJob.class)
                    .withIdentity("notification-cleanup-job")
                    .storeDurably()
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("notification-cleanup-trigger")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 0 3 * * ?")
                    )
                    .forJob(jobDetail)
                    .build();

            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, trigger);
            }

        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}