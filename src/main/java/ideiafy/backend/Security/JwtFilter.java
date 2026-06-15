package ideiafy.backend.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException{
                String authHeader = request.getHeader("Authorization");

                if(authHeader != null && authHeader.startsWith("Bearer ")){
                    String token = authHeader.substring(7);
                    UUID userid = JwtUtil.getUserId(token);

                    if(userid != null){
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userid,
                                        null,
                                        Collections.emptyList()
                                );
                        SecurityContextHolder.getContext()
                                .setAuthentication(authentication);
                    }

                }
                filterChain.doFilter(request, response);
            }
}