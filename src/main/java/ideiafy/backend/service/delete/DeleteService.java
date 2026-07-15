package ideiafy.backend.service.delete;

import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.service.delete.jobs.PermanentDeleteJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerKey.triggerKey;

@Service
public class DeleteService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private UserRepository repository;

    private JobKey jobKey(UUID itemId) {
        return JobKey.jobKey("delete-item-" + itemId, "permanent-delete-group");
    }

    private TriggerKey triggerKey(UUID itemId) {
        return TriggerKey.triggerKey("trigger-item-" + itemId, "permanent-delete-group");
    }

    public void permanentDelete(UUID id) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(PermanentDeleteJob.class)
                    .withIdentity(jobKey(id))
                    .usingJobData("itemId", id.toString())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey(id))
                    .startAt(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            throw new RuntimeException("Erro ao agendar exclusão permanente.", e);
        }
    }

}
