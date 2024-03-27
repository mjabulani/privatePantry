package com.mjabulani.privatePantry.api;

import com.mjabulani.privatePantry.model.user.UserEntity;
import com.mjabulani.privatePantry.model.user.UserResponse;
import com.mjabulani.privatePantry.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private UserEntity userEntity;
    private UserResponse userResponse;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @GetMapping(
            value = "users",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    List<UserResponse> getUsers() {
        List<UserResponse> usersList = new ArrayList<>();
        List<UserEntity> users = userRepository.findAll();

        for (UserEntity userEntity : users) {
            usersList.add(
                    UserResponse.builder()
                    .userId(userEntity.getUserId())
                    .userName(userEntity.getUserName())
                    .createdAt(userEntity.getCreatedAt().toLocalDate())
                    .active(userEntity.isActive())
                    .build()
                    );
        }
        return usersList;
    }

    @GetMapping(
            value = "users/{id}",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    UserResponse getUser(@PathVariable int id) {
        UserEntity user =  userRepository.findById(id);
        return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getCreatedAt().toLocalDate(),
                user.isActive());
    }

}
