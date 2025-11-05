package com.example.springFirstProject.DTO.userdto;

import com.example.springFirstProject.Enums.UserRole;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String responseId;

    private String userFirstName;
    private String userLastName;
    private String email;
    private String phoneNumber;
    private UserRole userRole;
    private List<AddressDTO> addressDTO;

}
