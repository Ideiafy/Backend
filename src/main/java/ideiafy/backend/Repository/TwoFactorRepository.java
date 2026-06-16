package ideiafy.backend.Repository;

import ideiafy.backend.model.TwoFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TwoFactorRepository extends JpaRepository<TwoFactor, UUID> {
    TwoFactor findByEmail(String email);
}
