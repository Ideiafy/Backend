package ideiafy.backend.service.auth;

import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.Security.SecurityUtils;
import ideiafy.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository repository;

    public User getCurrentUser(){
        UUID userId = SecurityUtils.getAuthenticationUserId();
        return repository.findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        "User not authenticated"
                ));
    }
}
