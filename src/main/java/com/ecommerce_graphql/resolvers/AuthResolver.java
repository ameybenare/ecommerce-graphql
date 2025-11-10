package com.ecommerce_graphql.resolvers;

import com.ecommerce_graphql.DTO.UserDTO;
import com.ecommerce_graphql.service.AuthService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthResolver {

    private final AuthService authService;

    public AuthResolver(AuthService authService) {
        this.authService = authService;
    }

    

    @MutationMapping
    public UserDTO login(@Argument String email, @Argument String password) {
        //LoginRequestDTO loginRequest = new LoginRequestDTO();
        //loginRequest.setEmail(email);
        //loginRequest.setPassword(password);
        return authService.login(email,password);
    }
}
