package ideiafy.backend.service.queue;

import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.service.DeleteService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;

public class PermanentDeleteJob implements Job {

    @Autowired
    private UserRepository repository;

    @Override
    public void execute(JobExecutionContext context){
        String itemIdStr = context.getJobDetail().getJobDataMap().getString("itemId");
        UUID itemId = java.util.UUID.fromString(itemIdStr);

       repository.deleteById(itemId);
    }
}
