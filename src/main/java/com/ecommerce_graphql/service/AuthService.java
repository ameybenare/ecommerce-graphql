package com.ecommerce_graphql.service;


import com.ecommerce_graphql.DTO.UserDTO;
import com.ecommerce_graphql.model.User;
import com.ecommerce_graphql.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register user
    public UserDTO register(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        //user.setPassword(dto.getPassword()); // in real life: hash password

        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    // Login
    public UserDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        return mapToDTO(user);
    }

    // Mapping
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        //dto.setPassword(user.getPassword()); // careful in real apps
        return dto;
    }
}
