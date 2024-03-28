package com.mjabulani.privatePantry.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserAddRequest {

    private  String userName;
    private String userPassword;

}
