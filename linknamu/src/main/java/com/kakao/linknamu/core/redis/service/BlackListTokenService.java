package com.kakao.linknamu.core.redis.service;

import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.redis.RedisExceptionStatus;
import com.kakao.linknamu.core.redis.entity.BlackListToken;
import com.kakao.linknamu.core.redis.repository.BlackListTokenRepository;
import com.kakao.linknamu.core.security.JwtProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListTokenService {
	private final BlackListTokenRepository blackListTokenRepository;

	public void save(String accessToken) {
		Long remainTime = JwtProvider.getRemainExpiration(accessToken);
		BlackListToken blackListToken = BlackListToken.builder()
			.accessToken(accessToken)
			.expiration(remainTime)
			.build();

		blackListTokenRepository.save(blackListToken);
	}

	public void validAccessToken(String accessToken) {
		if (blackListTokenRepository.existsById(accessToken)) {
			throw new Exception403(RedisExceptionStatus.BLACKLIST_TOKEN);
		}
	}
}
