package ideiafy.backend.controller;

import ideiafy.backend.dto.UserDto;
import ideiafy.backend.model.User;
import ideiafy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @PostMapping
    public ResponseEntity createUser(@RequestBody UserDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable Integer id){
        service.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User was deleted");
    }
    @PutMapping("/{id}")
    public ResponseEntity putUser( @PathVariable Integer id, @RequestBody UserDto dto){
        return ResponseEntity.ok(service.putUser(id,dto));
    }
}
