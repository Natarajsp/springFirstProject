package com.example.springFirstProject.DTO.userdto;

import lombok.Data;

import java.util.List;

@Data
public class UserRequestDTO {
    private String userFirstName;
    private String userLastName;
    private String email;
    private String phoneNumber;
//    private UserRole userRole;
    private List<AddressDTO> addressDTO;
}
