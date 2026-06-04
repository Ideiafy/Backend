package ideiafy.backend.controller;

import ideiafy.backend.dto.ChangePasswordDto;
import ideiafy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService service;

    @GetMapping
    public ResponseEntity getAll(){
        return ResponseEntity.ok(service.getAllUsers());
    }
    @GetMapping("/me")
    public ResponseEntity getMyUser(){
        return ResponseEntity.ok(service.getMyUser());
    }
    @PutMapping("/me/password")
    public ResponseEntity<Void> putUser(@RequestBody ChangePasswordDto dto){
        service.changePassword(dto);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(){
        service.deleteUser();
        return ResponseEntity.noContent().build();
    }
}
