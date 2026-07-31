package ideiafy.backend.service.iterations.like;

import ideiafy.backend.Repository.LikeRepository;
import ideiafy.backend.model.iterations.Like;
import ideiafy.backend.model.Post;
import ideiafy.backend.model.User;
import ideiafy.backend.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public boolean toggleLike(Post post, User user){
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


}
