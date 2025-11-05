package com.example.springFirstProject.Service;

import com.example.springFirstProject.Beans.Addresses;
import com.example.springFirstProject.Beans.User;
import com.example.springFirstProject.DTO.userdto.AddressDTO;
import com.example.springFirstProject.DTO.userdto.UserDTO;
import com.example.springFirstProject.DTO.userdto.UserRequestDTO;
import com.example.springFirstProject.DTO.userdto.UserResponseDTO;
import com.example.springFirstProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
//    List<User> userList = new ArrayList<>();
//    private long userId = 1L;

    @Autowired
    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getAllUsers() {
        List<UserResponseDTO> userResponseList = userRepository.findAll().stream()
                .map(this::userResponseFormater).collect(Collectors.toList());

        if (!userResponseList.isEmpty()) return new ResponseEntity<>(userResponseList, HttpStatus.OK);
        else return new ResponseEntity<>("UserList is Empty!!!", HttpStatusCode.valueOf(200));
    }

    public Optional<UserResponseDTO> getUserById (long id) {
//        return userList.stream().filter(user -> user.getUserId() == id)
//                .findFirst();
        return userRepository.findById(id).map(this::userResponseFormater);
    }

    public ResponseEntity<String> addUserInfo(UserRequestDTO userRequest) {
//        user.setUserId(userId++);
//        userList.add(user);
//        if (userList.contains(user))
//            return new ResponseEntity<>("User Added Successfully.", HttpStatusCode.valueOf(201));
//        else
//            return new ResponseEntity<>("Something went wrong while adding new user into the List!", HttpStatusCode.valueOf(405));

//        user.setUserId(0);
        System.out.println(userRequest.getAddressDTO());
        User user = new User();
        userRequestFormater(user, userRequest);
        User addedUser = userRepository.save(user);
        if (userRepository.existsById(addedUser.getUserId()))
            return new ResponseEntity<>("User Added Successfully.", HttpStatusCode.valueOf(201));
        else
            return new ResponseEntity<>("Something went wrong while adding new user into the List!", HttpStatusCode.valueOf(405));

    }

    public ResponseEntity<String> updateUser(long userId, UserRequestDTO userRequest) {

//        return userList.stream().filter(user1 -> user1.getUserId() == userId)
//                .findFirst()
//                .map(user1 -> {
//                    user1.setUserFirstName(user.getUserFirstName());
//                    user1.setUserLastName(user.getUserLastName());
//                    return new ResponseEntity<> ("User Updated Successfully.", HttpStatusCode.valueOf(200));
//                }).orElse(new ResponseEntity<> ("User Not Found!!!", HttpStatusCode.valueOf(404)));

//        Optional<User> existingUser = userRepository.findById(userId);
        return userRepository.findById(userId).map(
                existingUser -> {
                    userRequestFormater(existingUser, userRequest);
                    System.out.println(existingUser.toString());
                    userRepository.save(existingUser);
                    return new ResponseEntity<> ("User Updated Successfully.", HttpStatusCode.valueOf(200));
                }).orElse(new ResponseEntity<> ("User Not Found!!!", HttpStatusCode.valueOf(404)));

    }

    public ResponseEntity<?> deleteUserById(long userId) {
//        Optional<User> existingUser = getUserById(userId);
//        if (existingUser.isPresent()) {
//            userList.remove(existingUser.get());
//            return ResponseEntity.status(HttpStatusCode.valueOf(200))
//                    .body(new UserDTO(existingUser.get(), "User Removed Successfully."));
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body( "User Not Found!!!");

        Optional<UserResponseDTO> existingUser = userRepository.findById(userId).map(this::userResponseFormater);
        if (existingUser.isPresent()) {
            userRepository.deleteById(userId);
            return ResponseEntity.status(HttpStatusCode.valueOf(200))
                    .body(new UserDTO(existingUser.get(), "User Removed Successfully."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body( "User Not Found!!!");
    }

    private void userRequestFormater (User user, UserRequestDTO userRequest) {
        user.setUserFirstName(userRequest.getUserFirstName());
        user.setUserLastName(userRequest.getUserLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        System.out.println(userRequest.getAddressDTO());
        if(userRequest.getAddressDTO() != null) {
            if (user.getAddressesList() == null) {
                user.setAddressesList(new ArrayList<>());
            } else {
                user.getAddressesList().clear();
            }
            for (AddressDTO address : userRequest.getAddressDTO()) {
                Addresses addrs = new Addresses();
                addrs.setStreet(address.getStreet());
                addrs.setCity(address.getCity());
                addrs.setState(address.getState());
                addrs.setCountry(address.getCountry());
                addrs.setZipcode(address.getZipcode());
                user.getAddressesList().add(addrs);
            }
        } else {
            System.out.println("Address DTO is Empty.");
        }
    }

    private UserResponseDTO userResponseFormater (User user) {
        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setResponseId(String.valueOf(user.getUserId()));
        userResponse.setUserFirstName(user.getUserFirstName());
        userResponse.setUserLastName(user.getUserLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setUserRole(user.getUserRole());
        if (user.getAddressesList() != null || !user.getAddressesList().isEmpty()) {
            List<AddressDTO> addressDTOList = new ArrayList<>();
            for (Addresses addrs : user.getAddressesList()) {
                AddressDTO address = new AddressDTO();
                address.setStreet(addrs.getStreet());
                address.setCity(addrs.getCity());
                address.setState(addrs.getState());
                address.setCountry(addrs.getCountry());
                address.setZipcode(addrs.getZipcode());
                addressDTOList.add(address);
            }
            userResponse.setAddressDTO(addressDTOList);

        } else {
            System.out.println("Address List Is Not Found!!!");
        }
        return userResponse;
    }

}
