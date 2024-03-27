package com.mjabulani.privatePantry.model.user;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;


@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int userId;

    @Column(name ="user_name")
    private String userName;

    @Column(name = "user_password")
    private String password;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "active")
    private boolean active;


}
