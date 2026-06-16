package ideiafy.backend.service;

import ideiafy.backend.Inputs.VerifyCodeInput;
import ideiafy.backend.Repository.TwoFactorRepository;
import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.Security.JwtUtil;
import ideiafy.backend.model.TwoFactor;
import ideiafy.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TwoFactorService {
    @Autowired
    TwoFactorRepository repository;
    @Autowired
    UserRepository userRepository;

    private final SecureRandom random = new SecureRandom();

    private String generateCode(){
        return String.format(
                "%06d",
                random.nextInt(1000000)
        );
    }
    public void createCode(String email){
        TwoFactor oldCode = repository.findByEmail(email);

        if(oldCode!= null){
            repository.delete(oldCode);
        }

        String code = generateCode();

        TwoFactor twoFactor = new TwoFactor();
        twoFactor.setEmail(email);
        twoFactor.setCode(code);
        twoFactor.setExpireAt(
                LocalDateTime.now().plusMinutes(5)
        );

        repository.save(twoFactor);
    }
    public String verifyCode(VerifyCodeInput input){
        TwoFactor savedCode = repository.findByEmail(input.email());
        if(savedCode == null){
            throw new RuntimeException("Code not found");
        }
        if(savedCode.getExpireAt().isBefore(LocalDateTime.now())){
            repository.delete(savedCode);
            throw new RuntimeException("Expired Code");
        }
        if(!savedCode.getCode().equals(input.code())){
            throw new RuntimeException("Wrong Code");
        }

        User user = userRepository.findByEmail(input.email());

        if(user == null){
            throw new RuntimeException("User not found");
        }
        repository.delete(savedCode);
        return JwtUtil.generateToken(user.getId(),user.getEmail());
    }

}
