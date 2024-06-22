package com.example.Online.Book.Store.dto;

import com.example.Online.Book.Store.entity.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String role;

    public static UserDTO userEntityToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setFirst_name(user.getFirst_name());
        userDTO.setLast_name(user.getLast_name());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
