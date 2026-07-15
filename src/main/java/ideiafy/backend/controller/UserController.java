package ideiafy.backend.controller;

import ideiafy.backend.model.User;
import ideiafy.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService service;

    @QueryMapping
    public List<User> users(){
        return service.getAllUsers();
    }
    @QueryMapping
    public User user(){
        return service.getMyUser();
    }
    @MutationMapping
    public Boolean deleteUser(){
        service.deleteUser();
        return true;
    }
}
