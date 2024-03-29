package com.mjabulani.privatePantry.api;

import com.mjabulani.privatePantry.model.user.UserAddRequest;
import com.mjabulani.privatePantry.model.user.UserEntity;
import com.mjabulani.privatePantry.model.user.UserResponse;
import com.mjabulani.privatePantry.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    UserController(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;

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
        UserEntity user = userRepository.findById(id);
        return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getCreatedAt().toLocalDate(),
                user.isActive());
    }

    @PostMapping(
            value = "users/add",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    UserResponse createUser(@RequestBody UserAddRequest userAddRequest) throws Exception {

        if (userValidator.validateUserName(userAddRequest.getUserName())) {
            if (userValidator.validateUserPassword(userAddRequest.getUserPassword())) {
                Date now = Date.valueOf(LocalDate.now());
                UserEntity userEntity = new UserEntity(0,
                        userAddRequest.getUserName(),
                        userAddRequest.getUserPassword(),
                        now,
                        true);
                userRepository.save(userEntity);
                return new UserResponse(
                        userEntity.getUserId(),
                        userEntity.getUserName(),
                        now.toLocalDate(),
                        true);
            } else {
                throw new BadRequestException("Invalid password. U need to use at least 8 characters and use !@#$ chars.");
            }
        } else {
            throw new BadRequestException("User already exists");
        }
    }

    @PutMapping(
            value = "users/{id}/deactivate",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    String deactivateUser(@PathVariable int id) throws BadRequestException {
        if (userRepository.existsById(id)) {
            UserEntity user = userRepository.findById(id);
            if (!userValidator.isUserActive(id)) {
                throw new BadRequestException(("User is already inactive!"));
            } else {
                user.setActive(false);
                userRepository.save(user);
            }
        } else {
            throw new BadRequestException("User does not exist");
        }
        return "OK";
    }

    @PutMapping(
            value = "users/{id}/activate",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    String activateUser(@PathVariable int id) throws BadRequestException {
        if (userRepository.existsById(id)) {
            UserEntity user = userRepository.findById(id);
            if (userValidator.isUserActive(id)) {
                throw new BadRequestException(("User is already active"));
            } else {
                user.setActive(true);
                userRepository.save(user);
            }
        } else {
            throw new BadRequestException("User does not exist");
        }
    return "OK";
    }

}
