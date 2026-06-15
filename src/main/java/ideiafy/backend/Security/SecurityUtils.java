package ideiafy.backend.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {
    public static UUID getAuthenticationUserId(){
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if(auth == null || auth.getPrincipal() == null){
            throw new RuntimeException("Invalid token");
        }
        return (UUID) auth.getPrincipal();
    }
}
