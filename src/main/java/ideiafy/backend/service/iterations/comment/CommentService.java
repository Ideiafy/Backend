package ideiafy.backend.service.iterations.comment;

import ideiafy.backend.Repository.CommentRepository;
import ideiafy.backend.model.Post;
import ideiafy.backend.model.User;
import ideiafy.backend.model.iterations.Comment;
import ideiafy.backend.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Comment createComment(Post post, User user, String content){
        Comment newComment = Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
        commentRepository.save(newComment);

        notificationService.notifyComment(post,user);
        return newComment;
    }
    @Transactional
    public boolean deleteComment(UUID id,User user){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if(!comment.getId().equals(user.getId())){
            throw new RuntimeException("You cannot delete this comment.");
        }
        commentRepository.delete(comment);

        return true;
    }
}
