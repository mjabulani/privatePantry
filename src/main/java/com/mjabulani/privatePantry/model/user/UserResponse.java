package com.mjabulani.privatePantry.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponse {


    private int userId;
    private String userName;
    private LocalDate createdAt;
    private boolean active;
}
