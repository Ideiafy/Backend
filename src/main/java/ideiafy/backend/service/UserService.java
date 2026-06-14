package ideiafy.backend.service;

import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.Security.JwtUtil;
import ideiafy.backend.Security.SecurityUtils;
import ideiafy.backend.Inputs.ChangePasswordInput;
import ideiafy.backend.Inputs.LoginInput;
import ideiafy.backend.Inputs.UserInput;
import ideiafy.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository repository;

    @Autowired
    BCryptPasswordEncoder encoder;

    public List<User> getAllUsers(){
        return repository.findAll();
    }
    public User getMyUser(){
        User user = repository.findById(SecurityUtils.getAuthenticationUserId()).orElseThrow(()->
                new RuntimeException("User not found"));
        return user;
    }
    public User createUser(UserInput input){
        User userExists = repository.findByEmail(input.email());

        if(userExists != null){
            throw new RuntimeException("Email Already registered");
        }

        return repository.save(toEntity(input));
    }
    public void deleteUser(){
        User user = repository.findById(SecurityUtils.getAuthenticationUserId()).orElseThrow(()->
                new RuntimeException("User not found"));

        repository.delete(user);
    }
    public void changePassword(ChangePasswordInput input){
        User user = repository.findById(SecurityUtils.getAuthenticationUserId()).orElseThrow(()->
                new RuntimeException("User not found"));
        if(!encoder.matches(input.oldPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Wrong Password"
            );
        }
        user.setPassword(encoder.encode(input.newPassword()));
        repository.save(user);
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
