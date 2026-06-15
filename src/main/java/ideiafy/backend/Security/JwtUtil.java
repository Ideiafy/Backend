package ideiafy.backend.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    private static final String SECRET = "secret_my_key_guh";


    public static String generateToken(UUID userId, String email){
        return JWT.create()
                .withSubject(email)
                .withClaim("id", userId.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static UUID getUserId(String token) {
        try {
            String id = JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(token)
                    .getClaim("id")
                    .asString();

            return UUID.fromString(id);
        } catch (Exception e) {
            return null;
        }
    }

}