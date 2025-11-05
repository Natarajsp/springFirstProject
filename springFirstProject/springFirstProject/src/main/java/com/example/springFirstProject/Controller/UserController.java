package com.example.springFirstProject.Controller;

import com.example.springFirstProject.DTO.userdto.UserRequestDTO;
import com.example.springFirstProject.DTO.userdto.UserResponseDTO;
import com.example.springFirstProject.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/getUser")
    public ResponseEntity<?> getAllUsers () {
        return userService.getAllUsers();
    }

    @GetMapping("/api/getUserById/{id}")
    public ResponseEntity<?> getUserById (@PathVariable long id) {
        Optional<UserResponseDTO> userResponse = userService.getUserById(id);
        if (userResponse.isPresent()) {
            return new ResponseEntity<>(userResponse.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("User Not Present In the List!!!", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/api/addUser")
    public ResponseEntity<String> addUserInfo(@RequestBody UserRequestDTO userRequest) {
        return userService.addUserInfo(userRequest);
    }

    @PutMapping("/api/updateUser/{userId}")
    public ResponseEntity<String> updateUser (@PathVariable long userId, @RequestBody UserRequestDTO userRequest) {
        return userService.updateUser (userId, userRequest);
    }

    @DeleteMapping("/api/deleteUserById/{userid}")
    public ResponseEntity<?> deleteUserById (@PathVariable("userid") long userId) {
        return userService.deleteUserById (userId);
    }
}
