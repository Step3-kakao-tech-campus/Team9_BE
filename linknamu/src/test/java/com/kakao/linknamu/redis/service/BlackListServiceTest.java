package com.kakao.linknamu.redis.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.redis.RedisExceptionStatus;
import com.kakao.linknamu.core.redis.entity.BlackListToken;
import com.kakao.linknamu.core.redis.repository.BlackListTokenRepository;
import com.kakao.linknamu.core.redis.service.BlackListTokenService;
import com.kakao.linknamu.core.security.JwtProvider;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;

@ExtendWith(MockitoExtension.class)
public class BlackListServiceTest {
	@InjectMocks
	private BlackListTokenService blackListTokenService;

	@Mock
	private BlackListTokenRepository blackListTokenRepository;

	@BeforeEach
	void setup() {
		JwtProvider.REFRESH_SECRET = UUID.randomUUID().toString();
		JwtProvider.ACCESS_SECRET = UUID.randomUUID().toString();
	}

	@DisplayName("블랙리스트 토큰 저장 테스트")
	@Nested
	class BlackListTokenSaveTest {
		@DisplayName("Access토큰을 입력하면 블랙리스트에 저장이 된다")
		@Test
		void success() {
			// given
			User user = User.builder()
				.userId(1L)
				.email("rjsdnxogh55@gmail.com")
				.role(Role.ROLE_USER)
				.provider(Provider.PROVIDER_GOOGLE)
				.build();
			String accessToken = JwtProvider.create(user);
			Long expiration = JwtProvider.getRemainExpiration(accessToken);
			BlackListToken blackListToken = BlackListToken.builder()
				.accessToken(accessToken)
				.expiration(expiration)
				.build();

			ArgumentCaptor<BlackListToken> captorBlackList = ArgumentCaptor.forClass(BlackListToken.class);

			// mock
			given(blackListTokenRepository.save(any())).willReturn(blackListToken);

			// when
			blackListTokenService.save(accessToken);

			// then
			verify(blackListTokenRepository, times(1)).save(captorBlackList.capture());
			assertEquals(blackListToken, captorBlackList.getValue());

		}
	}

	@DisplayName("Access 토큰이 블랙리스트에 존재하는 지 테스트")
	@Nested
	class ValidAccessTokenTest {
		@DisplayName("Access 토큰이 블랙리스트에 없다면 유효한 토큰이다")
		@Test
		void validAccessToken() {
			// given
			User user = User.builder()
				.userId(1L)
				.email("rjsdnxogh55@gmail.com")
				.role(Role.ROLE_USER)
				.provider(Provider.PROVIDER_GOOGLE)
				.build();
			String accessToken = JwtProvider.create(user);
			Long expiration = JwtProvider.getRemainExpiration(accessToken);

			// mock
			given(blackListTokenRepository.existsById(accessToken)).willReturn(false);

			// when
			blackListTokenService.validAccessToken(accessToken);

			// then
		}

		@DisplayName("Access 토큰이 블랙리스트에 있다면 예외를 발생시킨다")
		@Test
		void invalidAccessToken() {
			// given
			User user = User.builder()
				.userId(1L)
				.email("rjsdnxogh55@gmail.com")
				.role(Role.ROLE_USER)
				.provider(Provider.PROVIDER_GOOGLE)
				.build();
			String accessToken = JwtProvider.create(user);
			Long expiration = JwtProvider.getRemainExpiration(accessToken);

			// mock
			given(blackListTokenRepository.existsById(accessToken)).willReturn(true);

			// when
			Throwable exception = assertThrows(Exception403.class,
				() -> blackListTokenService.validAccessToken(accessToken));

			// then
			assertEquals(RedisExceptionStatus.BLACKLIST_TOKEN.getMessage(), exception.getMessage());
		}
	}
}
