package ideiafy.backend.controller;

import ideiafy.backend.model.Notification;
import ideiafy.backend.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @MutationMapping
    public boolean markNotificationAsRead(@Argument UUID id){
        return notificationService.markNotificationAsRead(id);
    }
    @MutationMapping
    public boolean deleteNotification(@Argument UUID id){
        return notificationService.deleteNotification(id);
    }

    @QueryMapping
    public List<Notification> myNotifications(){
        return notificationService.myNotifications();
    }
}
