package ideiafy.backend.service.iterations.follow;

import ideiafy.backend.Repository.FollowRepository;
import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.model.User;
import ideiafy.backend.model.iterations.Follow;
import ideiafy.backend.service.auth.AuthenticationService;
import ideiafy.backend.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public boolean toggleFollow(UUID followingId){
        User follower =  authenticationService.getCurrentUser();
        User following = userRepository.findById(followingId).orElseThrow(
                ()-> new RuntimeException("User not found"));

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
    public long countByFollowing(UUID followingId){
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return followRepository.countByFollowing(following);
    }
    public long countByFollower(UUID followerId){
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return followRepository.countByFollower(follower);
    }
}
