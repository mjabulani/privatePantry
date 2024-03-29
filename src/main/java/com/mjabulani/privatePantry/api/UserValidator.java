package com.mjabulani.privatePantry.api;

import com.mjabulani.privatePantry.model.user.UserAddRequest;
import com.mjabulani.privatePantry.model.user.UserEntity;
import com.mjabulani.privatePantry.model.user.UserResponse;
import com.mjabulani.privatePantry.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserValidator {
    @Autowired
    UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final int PASSWORD_LENGTH = 8;
    Pattern letter = Pattern.compile("[a-zA-z]");
    Pattern digit = Pattern.compile("[0-9]");
    Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

    public UserValidator() {
    }

    public boolean validateUserName(String userName) {

        if (!(userRepository.findByUserName(userName).size() > 0)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateUserPassword(String userPassword) {
        if (userPassword.length() > PASSWORD_LENGTH) {
            Matcher hasLetter = letter.matcher(userPassword);
            Matcher hasDigit = digit.matcher(userPassword);
            Matcher hasSpecial = special.matcher(userPassword);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();
        } else {
            return false;
        }
    }

    public boolean isUserActive(int userId) {
        return userRepository.findById(userId).isActive();
    }
}
