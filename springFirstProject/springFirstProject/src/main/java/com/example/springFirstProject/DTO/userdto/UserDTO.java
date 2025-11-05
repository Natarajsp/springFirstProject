package com.example.springFirstProject.DTO.userdto;

import lombok.Data;

@Data
public class UserDTO {
    private UserResponseDTO userResponse;
    private String statusMessage;

    public UserDTO(UserResponseDTO userResponse, String statusMessage) {
        this.userResponse = userResponse;
        this.statusMessage = statusMessage;
    }
}
