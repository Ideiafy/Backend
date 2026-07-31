package ideiafy.backend.Repository;

import ideiafy.backend.model.User;
import ideiafy.backend.model.iterations.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID> {
    boolean existByFollowerAndFollowing(User follower, User Following);
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    long countByFollower(User follower);
    long countByFollowing(User following);
}
