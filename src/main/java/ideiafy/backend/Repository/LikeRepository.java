package ideiafy.backend.Repository;

import ideiafy.backend.model.iterations.Like;
import ideiafy.backend.model.Post;
import ideiafy.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    boolean existsByUserAndPost(User user, Post post);
    Optional<Like> findByUserAndPost(User user, Post post);
    Long countByPost(Post post);
}
