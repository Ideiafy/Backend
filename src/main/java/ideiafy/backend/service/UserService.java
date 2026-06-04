package ideiafy.backend.service;

import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.Security.JwtUtil;
import ideiafy.backend.Security.SecurityUtils;
import ideiafy.backend.dto.ChangePasswordDto;
import ideiafy.backend.dto.LoginDto;
import ideiafy.backend.dto.UserDto;
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
    public User createUser(UserDto dto){
        User userExists = repository.findByEmail(dto.email());

        if(userExists != null){
            throw new RuntimeException("Email Already registered");
        }

        return repository.save(toEntity(dto));
    }
    public void deleteUser(){
        User user = repository.findById(SecurityUtils.getAuthenticationUserId()).orElseThrow(()->
                new RuntimeException("User not found"));

        repository.delete(user);
    }
    public void changePassword(ChangePasswordDto dto){
        User user = repository.findById(SecurityUtils.getAuthenticationUserId()).orElseThrow(()->
                new RuntimeException("User not found"));
        if(!encoder.matches(dto.oldPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Wrong Password"
            );
        }
        user.setPassword(encoder.encode(dto.newPassword()));
        repository.save(user);
    }
    public String Login(LoginDto dto){
        User user = repository.findByEmail(dto.email());

        if(user == null){
            throw new RuntimeException("User not found");
        }
        if(!encoder.matches(dto.password(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Wrong Password"
            );
        }
        return JwtUtil.generateToken(user.getId(),user.getEmail());
    }

    private User toEntity(UserDto dto){
        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(encoder.encode(dto.password()))
                .build();
    }

}
