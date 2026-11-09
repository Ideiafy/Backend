package ideiafy.backend.service.notification;

import ideiafy.backend.Repository.NotificationRepository;
import ideiafy.backend.model.Notification;
import ideiafy.backend.model.NotificationType;
import ideiafy.backend.model.Post;
import ideiafy.backend.model.User;
import ideiafy.backend.service.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationMessageService messageService;
    @Autowired
    private AuthenticationService authenticationService;

    public void notifyLike(Post post, User sender) {
        createNotification(
                post.getUser(),
                sender,
                NotificationType.LIKE,
                post
        );
    }

    public void notifyComment(Post post, User sender) {
        createNotification(
                post.getUser(),
                sender,
                NotificationType.COMMENT,
                post
        );
    }

    public void notifyFollow(User sender, User receiver) {
        createNotification(
                receiver,
                sender,
                NotificationType.FOLLOW,
                null
        );
    }


    private void createNotification(
            User receiver,
            User sender,
            NotificationType type,
            Post post
    ) {
        String postTitle = post != null
                ? post.getTitle()
                : null;

        String body = messageService.getBody(
                sender.getName(),
                type,
                postTitle
        );


        Notification notification = Notification.builder()
                .receiver(receiver)
                .sender(sender)
                .post(post)
                .type(type)
                .body(body)
                .read(false)
                .build();

        notificationRepository.save(notification);
    }

    public boolean markNotificationAsRead(UUID id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new RuntimeException("notification not found"));
        User user = authenticationService.getCurrentUser();
        if (!notification.getReceiver().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot modify this notification");
        }
        notification.setRead(true);
        notificationRepository.save(notification);
        return true;
    }
    public boolean deleteNotification(UUID id){
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new RuntimeException("notification not found"));
        User user = authenticationService.getCurrentUser();
        if (!notification.getReceiver().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot modify this notification");
        }
        notificationRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void deleteExpiredNotifications() {

        notificationRepository.deleteOlderThan(
                LocalDateTime.now().minusDays(30)
        );

    }
    public List<Notification> myNotifications(){
        User user = authenticationService.getCurrentUser();
        return notificationRepository.findByReceiver(user);
    }

}
