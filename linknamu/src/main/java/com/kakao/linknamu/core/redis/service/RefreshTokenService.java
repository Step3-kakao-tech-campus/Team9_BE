package com.kakao.linknamu.core.redis.service;

import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.core.redis.RedisExceptionStatus;
import com.kakao.linknamu.core.redis.entity.RefreshToken;
import com.kakao.linknamu.core.redis.repository.RefreshTokenRepository;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {


	private final RefreshTokenRepository refreshTokenRepository;

	public void save(String refreshToken, String accessToken, User user) {
		RefreshToken token = RefreshToken.of(refreshToken, accessToken, user);
		refreshTokenRepository.save(token);
	}

	public void deleteById(String refreshToken) {
		refreshTokenRepository.deleteById(refreshToken);
	}

	public void deleteByAccessToken(String accessToken) {
		RefreshToken token = refreshTokenRepository.findByAccessToken(accessToken).orElseThrow(
			() -> new Exception404(RedisExceptionStatus.REFRESH_TOKEN_NOT_FOUND));

		refreshTokenRepository.deleteById(token.getRefreshToken());
	}

	public boolean existsById(String refreshToken) {
		return refreshTokenRepository.existsById(refreshToken);
	}
}
