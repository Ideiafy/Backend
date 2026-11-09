package ideiafy.backend.service.scheduler.jobs;

import ideiafy.backend.service.notification.NotificationService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationCleanupJob implements Job {
    @Autowired
    NotificationService notificationService;

    @Override
    public void execute(JobExecutionContext context){
        notificationService.deleteExpiredNotifications();
    }
}
