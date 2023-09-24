package com.kakao.linknamu.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(length = 10, nullable = false, unique = true, name = "nickname")
    private String nickname;


    @Builder
    public User(Long userId, String email, String password, String provider, String roles, String nickname) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.roles = roles;
        this.nickname = nickname;
    }
}
