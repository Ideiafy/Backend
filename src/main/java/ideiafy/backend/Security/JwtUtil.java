package ideiafy.backend.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.cdimascio.dotenv.Dotenv;


import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "secret_my_key_guh";

    public static String generateToken(Integer userId, String email){
        return JWT.create()
                .withSubject(email)
                .withClaim("id",userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static Integer getUserId(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(token)
                    .getClaim("id")
                    .asInt();
        } catch (Exception e) {
            System.out.println("Invalid Token" + e.getMessage());
            return null;
        }
    }

}