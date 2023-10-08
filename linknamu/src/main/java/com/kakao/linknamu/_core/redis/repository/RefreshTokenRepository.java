package com.kakao.linknamu._core.redis.repository;

import com.kakao.linknamu._core.redis.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findById(String refreshToken);
    Optional<RefreshToken> findByAccessToken(String accessToken);
}
