package ideiafy.backend.controller;

import ideiafy.backend.Inputs.ChangePasswordInput;
import ideiafy.backend.Inputs.LoginInput;
import ideiafy.backend.Inputs.UserInput;
import ideiafy.backend.service.TwoFactorService;
import ideiafy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthController {
    @Autowired
    UserService service;
    @Autowired
    TwoFactorService twoFactorService;

    @MutationMapping
    public String generateActivateUserCode(@Argument UserInput input){
        return service.generateActivateUserCode(input);
    }
    @MutationMapping
    public String login(@Argument LoginInput input){
        return service.login(input);
    }
    @MutationMapping
    public boolean activateUser(@Argument String email,@Argument String code){
        return service.activateUser(email,code);
    }
    @MutationMapping
    public String generatePasswordResetCode(){
        return service.generatePasswordReset();
    }
    @MutationMapping
    public boolean changePassword(@Argument String email, @Argument String code, @Argument ChangePasswordInput input){
        return service.changePassword(email,code,input);
    }

}
