package com.mjabulani.privatePantry.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="roles")
public class Role implements GrantedAuthority {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="role_id")
    private Integer roleId;


    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
