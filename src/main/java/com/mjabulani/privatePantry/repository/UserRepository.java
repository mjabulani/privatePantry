package com.mjabulani.privatePantry.repository;

import com.mjabulani.privatePantry.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController(path="users")
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findById(int id);

    List<UserEntity> findByUserName(String name);

    List<UserEntity> findByActive(boolean active);
}
