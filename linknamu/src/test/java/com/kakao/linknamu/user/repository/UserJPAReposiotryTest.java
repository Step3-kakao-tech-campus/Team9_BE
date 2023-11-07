package com.kakao.linknamu.user.repository;

import com.kakao.linknamu.core.config.QueryDslConfig;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaAuditing
@DataJpaTest
@Import(QueryDslConfig.class)
public class UserJPAReposiotryTest {
	private final UserJpaRepository userJPARepository;

	@Autowired
	public UserJPAReposiotryTest(UserJpaRepository userJPARepository) {
		this.userJPARepository = userJPARepository;
	}

	@DisplayName("디비에 회원 이메일이 있다면 회원 정보를 반환한다")
	@Test
	void userFindByEmailTest() {
		// given
		String findEmail = "rjsdnxogh@naver.com";
		User user = User.builder()
			.email("rjsdnxogh@naver.com")
			.password("123")
			.provider(Provider.PROVIDER_GOOGLE)
			.role(Role.ROLE_USER)
			.build();
		userJPARepository.save(user);

		// when
		User findUser = userJPARepository.findByEmail(findEmail).get();

		// then
		assertEquals(user, findUser);
	}

	@DisplayName("디비에 회원 이메일이 있다면 null 반환한다")
	@Test
	void userFindByEmailNullReturnTest() {
		// given
		String findEmail = "rjsdnxogh@naver.com";

		// when
		Optional<User> findUser = userJPARepository.findByEmail(findEmail);

		// then
		assertTrue(findUser.isEmpty());
	}
}
