package ideiafy.backend.Repository;

import ideiafy.backend.model.iterations.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

}
