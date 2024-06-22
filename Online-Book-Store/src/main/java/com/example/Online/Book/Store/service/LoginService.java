package com.example.Online.Book.Store.service;

import com.example.Online.Book.Store.dto.UserDTO;
import com.example.Online.Book.Store.entity.User;
import com.example.Online.Book.Store.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Transactional
    public boolean createUser(UserDTO userDTO) {
        User user = new User();
        user.setFirst_name(userDTO.getFirst_name());
        user.setLast_name(userDTO.getLast_name());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        UserDTO.userEntityToUserDTO(user);
        try {
            userRepository.save(user);
            logger.info("User registered successfully with email: " + user.getEmail());
            return true;
        } catch (Exception e) {
            logger.error("Error registering user", e);
            return false;
        }
    }
}
