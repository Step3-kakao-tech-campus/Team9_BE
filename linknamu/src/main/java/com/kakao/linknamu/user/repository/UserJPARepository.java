package com.kakao.linknamu.user.repository;

import com.kakao.linknamu.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJPARepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
