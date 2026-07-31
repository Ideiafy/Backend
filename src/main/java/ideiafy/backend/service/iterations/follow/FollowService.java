package ideiafy.backend.service.iterations.follow;

import ideiafy.backend.Repository.FollowRepository;
import ideiafy.backend.model.User;
import ideiafy.backend.model.iterations.Follow;
import ideiafy.backend.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public boolean toggleFollow(User follower, User following){

        if(follower.getId().equals(following.getId())){
            throw new RuntimeException("You can't follow yourself");
        }
        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowing(follower,following);
        if(existingFollow.isPresent()){
            followRepository.delete(existingFollow.get());
            return false;
        }

        Follow newFollow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        followRepository.save(newFollow);
        notificationService.notifyFollow(follower,following);
        return true;
    }
}
