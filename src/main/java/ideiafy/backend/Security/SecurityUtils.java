package ideiafy.backend.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Integer getAuthenticationUserId(){
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if(auth == null){
            throw new RuntimeException("Invalid token");
        }
        return (Integer) auth.getPrincipal();
    }
}
