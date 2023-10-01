package com.kakao.linknamu._core.redis.repository;

import com.kakao.linknamu._core.redis.entity.BlackListToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlackListTokenRepository extends CrudRepository<BlackListToken, String> {
    Optional<BlackListToken> findById(String accessToken);
}
