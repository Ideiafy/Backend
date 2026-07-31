package ideiafy.backend.service.auth;

import ideiafy.backend.model.TwoFactorType;
import ideiafy.backend.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class TwoFactorService {

    @Autowired
    private EmailService emailService;

    private final SecureRandom random = new SecureRandom();

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String buildKey(String email, TwoFactorType type){
        return type.name() + ":" + email;
    }

    public void generateCode(String email, TwoFactorType type){
        String code = String.format(
                "%06d",
                random.nextInt(1000000)
        );
        redisTemplate.opsForValue().set(
                buildKey(email,type),
                code,
                5,
                TimeUnit.MINUTES
        );
        emailService.sendCode(email,code, type);
    }

    public boolean validateCode(String email,String code, TwoFactorType type){
        String getCode = redisTemplate.opsForValue().get(buildKey(email,type));
        boolean valid = Objects.equals(getCode,code);
        if(valid){
            redisTemplate.delete(buildKey(email, type));
        }
        return valid;
    }


}
