package ideiafy.backend.service;

import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.Security.JwtUtil;
import ideiafy.backend.dto.LoginDto;
import ideiafy.backend.dto.UserDto;
import ideiafy.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User createUser(UserDto dto){
        return repository.save(toEntity(dto));
    }
    public void deleteUserById(Integer id){
        repository.deleteById(id);
    }
    public User putUser(Integer id,UserDto dto){
        User user = repository.findById(id).orElseThrow();
        updateEntity(user, dto);
        return repository.save(user);
    }

    public User toEntity(UserDto dto){
        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(encoder.encode(dto.password()))
                .build();
    }
    public void updateEntity(User user, UserDto dto){
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(encoder.encode(dto.password()));
    }

    public String Login(LoginDto dto){
        User user = repository.findByEmail(dto.email());

        if(user == null){
            throw new RuntimeException("User not found");
        }
        if(!encoder.matches(dto.password(), user.getPassword())){
            throw new RuntimeException("Wrong Password");
        }
        return JwtUtil.generateToken(user.getId(),user.getEmail());
    }

}
