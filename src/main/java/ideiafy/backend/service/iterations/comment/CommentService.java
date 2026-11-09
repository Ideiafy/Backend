package ideiafy.backend.service.iterations.comment;

import ideiafy.backend.Repository.CommentRepository;
import ideiafy.backend.Repository.PostsRepository;
import ideiafy.backend.model.Post;
import ideiafy.backend.model.User;
import ideiafy.backend.model.iterations.Comment;
import ideiafy.backend.service.auth.AuthenticationService;
import ideiafy.backend.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private PostsRepository postsRepository;

    @Transactional
    public Comment createComment(UUID postID, String content){
        User user = authenticationService.getCurrentUser();
        Post post = postsRepository.findById(postID).orElseThrow(
                ()-> new RuntimeException("Post not found"));
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
    public boolean deleteComment(UUID id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        User user = authenticationService.getCurrentUser();
        if(!comment.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You cannot delete this comment.");
        }
        commentRepository.delete(comment);

        return true;
    }
    public List<Comment> viewComments(UUID postId){
        return commentRepository.findByPostId(postId);
    }
}
