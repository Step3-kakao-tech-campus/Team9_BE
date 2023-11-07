package com.kakao.linknamu.core.redis.repository;

import com.kakao.linknamu.core.redis.entity.BlackListToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlackListTokenRepository extends CrudRepository<BlackListToken, String> {
	Optional<BlackListToken> findById(String accessToken);
}
