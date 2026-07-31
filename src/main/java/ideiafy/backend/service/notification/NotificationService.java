package ideiafy.backend.service.notification;

import ideiafy.backend.Repository.NotificationRepository;
import ideiafy.backend.model.Notification;
import ideiafy.backend.model.NotificationType;
import ideiafy.backend.model.Post;
import ideiafy.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationMessageService messageService;

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

        Notification notification = Notification.builder()
                .receiver(receiver)
                .sender(sender)
                .post(post)
                .type(type)
                .read(false)
                .build();

        notificationRepository.save(notification);
    }
}
