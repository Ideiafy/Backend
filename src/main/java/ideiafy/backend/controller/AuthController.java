package ideiafy.backend.controller;

import ideiafy.backend.Inputs.LoginInput;
import ideiafy.backend.Inputs.UserInput;
import ideiafy.backend.Inputs.VerifyCodeInput;
import ideiafy.backend.model.TwoFactor;
import ideiafy.backend.model.User;
import ideiafy.backend.service.TwoFactorService;
import ideiafy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class AuthController {
    @Autowired
    UserService service;

    @Autowired
    TwoFactorService twoFactorService;

    @MutationMapping
    public User createUser(@Argument UserInput input){
        return service.createUser(input);
    }
    @MutationMapping
    public String login(@Argument LoginInput input){
        return service.login(input);
    }
    @MutationMapping
    public String verifyCode(@Argument VerifyCodeInput input){
        return twoFactorService.verifyCode(input);
    }
}
