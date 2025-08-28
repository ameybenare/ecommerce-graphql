package com.ecommerce_graphql.resolvers;

import com.ecommerce_graphql.DTO.UserDTO;
import com.ecommerce_graphql.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserResolver {

    private final UserService userService;

    public UserResolver(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public UserDTO user(@Argument Long id) {
        return userService.getUserById(id);
    }
}
