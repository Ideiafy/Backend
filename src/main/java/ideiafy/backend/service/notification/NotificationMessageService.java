    package ideiafy.backend.service.notification;

    import ideiafy.backend.model.NotificationType;
    import org.springframework.stereotype.Service;

    @Service
    public class NotificationMessageService {
        public String getBody(String senderName, NotificationType type, String postTitle){
            return switch (type) {
                case FOLLOW ->
                        senderName + " started follow you.";

                case LIKE ->
                        senderName + " liked your post \"" + postTitle + "\".";

                case COMMENT ->
                        senderName + " comment in your post \"" + postTitle + "\".";
            };
        }
    }
