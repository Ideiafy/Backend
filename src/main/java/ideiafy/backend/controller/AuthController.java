package ideiafy.backend.controller;

import ideiafy.backend.dto.LoginDto;
import ideiafy.backend.dto.UserDto;
import ideiafy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService service;

    @PostMapping("/register")
    public ResponseEntity createUser(@RequestBody UserDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(dto));
    }
    @PostMapping("/login")
    public ResponseEntity Login(@RequestBody LoginDto dto){
        return ResponseEntity.ok(service.Login(dto));
    }
}
