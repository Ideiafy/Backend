package ideiafy.backend.service.iterations.like;

import ideiafy.backend.Repository.LikeRepository;
import ideiafy.backend.Repository.PostsRepository;
import ideiafy.backend.model.iterations.Like;
import ideiafy.backend.model.Post;
import ideiafy.backend.model.User;
import ideiafy.backend.service.auth.AuthenticationService;
import ideiafy.backend.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private PostsRepository postsRepository;

    @Transactional
    public boolean toggleLike(UUID postID){
        User user = authenticationService.getCurrentUser();
        Post post = postsRepository.findById(postID).orElseThrow(
                ()-> new RuntimeException("Post not found"));

        Optional<Like> like = likeRepository.findByUserAndPost(user,post);
        if(like.isPresent()){
            likeRepository.delete(like.get());
            return false;
        }
        Like newLike = Like.builder()
                .user(user)
                .post(post)
                .build();
        likeRepository.save(newLike);

        notificationService.notifyLike(post,user);
        return true;
    }
    public long countByPost(UUID postId){
        Post post = postsRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return likeRepository.countByPost(post);
    }


}
