package ideiafy.backend.Repository;

import ideiafy.backend.model.Notification;
import ideiafy.backend.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Modifying
    @Transactional
    @Query("""
        DELETE FROM Notification n
        WHERE n.createdAt < :date
    """)
    void deleteOlderThan(@Param("date") LocalDateTime date);
    List<Notification> findByReceiver(User receiver);
}
