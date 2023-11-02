package com.kakao.linknamu.redis.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kakao.linknamu.core.RedisContainerExtension;
import com.kakao.linknamu.core.redis.entity.BlackListToken;
import com.kakao.linknamu.core.redis.repository.BlackListTokenRepository;
import com.kakao.linknamu.core.security.JwtProvider;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({RedisContainerExtension.class, SpringExtension.class})
public class BlackListRepositoryTest {

	private final BlackListTokenRepository blackListTokenRepository;

	@Autowired
	public BlackListRepositoryTest(BlackListTokenRepository blackListTokenRepository) {
		this.blackListTokenRepository = blackListTokenRepository;
	}

	@DisplayName("Access 토큰으로 블랙 리스트를 조회할 수 있다")
	@Test
	void findByIdTest() {
		// given
		User user = User.builder().userId(1L)
			.email("rjsdnxogh@naver.com")
			.provider(Provider.PROVIDER_GOOGLE)
			.role(Role.ROLE_USER)
			.build();
		String accessToken = JwtProvider.create(user);
		Long expiration = JwtProvider.getRemainExpiration(accessToken);
		BlackListToken blackListToken = BlackListToken.builder()
			.accessToken(accessToken)
			.expiration(expiration)
			.build();
		blackListTokenRepository.save(blackListToken);

		// when
		Optional<BlackListToken> findByIdBlackListToken = blackListTokenRepository.findById(accessToken);

		//
		assertTrue(findByIdBlackListToken.isPresent());
		assertEquals(blackListToken, findByIdBlackListToken.get());
	}

	@DisplayName("Access 토큰이 블랙 리스트에 없다면 null을 반환한다")
	@Test
	void findByIdNullTest() {
		// given
		User user = User.builder().userId(1L)
			.email("rjsdnxogh@naver.com")
			.provider(Provider.PROVIDER_GOOGLE)
			.role(Role.ROLE_USER)
			.build();
		String accessToken = JwtProvider.create(user);

		// when
		Optional<BlackListToken> findByIdBlackListToken = blackListTokenRepository.findById(accessToken);

		//
		assertTrue(findByIdBlackListToken.isEmpty());
	}
}
