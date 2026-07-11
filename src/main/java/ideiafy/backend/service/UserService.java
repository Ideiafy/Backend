package ideiafy.backend.service;

import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.Security.JwtUtil;
import ideiafy.backend.Security.SecurityUtils;
import ideiafy.backend.Inputs.ChangePasswordInput;
import ideiafy.backend.Inputs.LoginInput;
import ideiafy.backend.Inputs.UserInput;
import ideiafy.backend.model.Status;
import ideiafy.backend.model.TwoFactorType;
import ideiafy.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository repository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    TwoFactorService twoFactorService;

    @Autowired
    DeleteService deleteService;


    public List<User> getAllUsers(){
        return repository.findAll();
    }
    public User getMyUser(){
        User user = repository.findById(SecurityUtils.getAuthenticationUserId()).orElseThrow(()->
                new RuntimeException("User not found"));
        return user;
    }
    public String generateActivateUserCode(UserInput input){
        User user = repository.findByEmail(input.email());

        if(user != null){
            throw new RuntimeException("Email Already registered");
        }
        repository.save(toEntity(input));
        twoFactorService.generateCode(input.email(), TwoFactorType.ACTIVATION);
        return "Code sent";
    }

    public boolean deleteUser(){
        User user = repository.findById(SecurityUtils.getAuthenticationUserId()).orElseThrow(()->
                new RuntimeException("User not found"));
        user.setActive(false);
        repository.save(user);
        deleteService.permanentDelete(user.getId());

        return true;
    }
    public String generatePasswordReset(){
        User user = repository.findById(SecurityUtils.getAuthenticationUserId()).orElseThrow(()->
                new RuntimeException("User not found"));
        twoFactorService.generateCode(user.getEmail(), TwoFactorType.PASSWORD_RESET);
        return "Code sent";
    }
    public boolean activateUser(String email,String code){
        if(!twoFactorService.validateCode(email,code, TwoFactorType.ACTIVATION)){
            throw new RuntimeException("Invalid Code");
        }
        User user = repository.findByEmail(email);
        if(user == null){
            throw new RuntimeException("User not found");
        }
        user.setStatus(Status.FINISHED);
        repository.save(user);
        return true;
    }
    public boolean changePassword(String email, String code, ChangePasswordInput input){
        if(!twoFactorService.validateCode(email,code, TwoFactorType.PASSWORD_RESET)){
            throw new RuntimeException("Invalid code");
        }
        User user = repository.findById(SecurityUtils.getAuthenticationUserId()).orElseThrow(()->
                new RuntimeException("User not found"));
        if(!encoder.matches(input.oldPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Wrong Password"
            );
        }
        user.setPassword(encoder.encode(input.newPassword()));
        repository.save(user);
        return true;
    }
    public String login(LoginInput input){
        User user = repository.findByEmail(input.email());

        if(user == null){
            throw new RuntimeException("User not found");
        }
        if(!encoder.matches(input.password(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Wrong Password"
            );
        }
        return JwtUtil.generateToken(user.getId(),user.getEmail());
    }


    private User toEntity(UserInput input){
        return User.builder()
                .name(input.name())
                .email(input.email())
                .password(encoder.encode(input.password()))
                .build();
    }

}
