package com.kakao.linknamu.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name="user_tb")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long userId;

    @Column(length = 100, nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(length = 30, nullable = false, name = "provider")
    private String provider;

    @Column(length = 30, nullable = false, name = "roles")
    private String roles;



    @Builder
    public User(Long userId, String email, String password, String provider, String roles) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getUserId(), user.getUserId()) && Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmail());
    }
}
