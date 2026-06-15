package ideiafy.backend.controller;

import ideiafy.backend.Inputs.LoginInput;
import ideiafy.backend.Inputs.UserInput;
import ideiafy.backend.model.User;
import ideiafy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthController {
    @Autowired
    UserService service;

    @MutationMapping
    public User createUser(@Argument UserInput input){
        return service.createUser(input);
    }
    @MutationMapping
    public String login(@Argument LoginInput input){
        return service.login(input);
    }
}
