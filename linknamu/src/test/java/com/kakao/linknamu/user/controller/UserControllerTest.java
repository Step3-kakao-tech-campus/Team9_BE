package com.kakao.linknamu.user.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kakao.linknamu.core.RedisContainerExtension;
import com.kakao.linknamu.core.RestDocs;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.core.redis.entity.RefreshToken;
import com.kakao.linknamu.core.redis.repository.BlackListTokenRepository;
import com.kakao.linknamu.core.redis.repository.RefreshTokenRepository;
import com.kakao.linknamu.core.security.JwtProvider;
import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.user.UserExceptionStatus;
import com.kakao.linknamu.user.dto.ReissueDto;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(RedisContainerExtension.class)
public class UserControllerTest extends RestDocs {

	private static final String TEST_USER_EMAIL = "rjsdnxogh55@gmail.com";

	@Autowired
	private UserJpaRepository userJPARepository;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private BlackListTokenRepository blackListTokenRepository;

	@BeforeEach
	void setup() {
		User user = User.builder().email(TEST_USER_EMAIL).role(Role.ROLE_USER)
			.provider(Provider.PROVIDER_GOOGLE).password("test1234").build();
		userJPARepository.save(user);
	}

	@DisplayName("토큰 재발행 통합 테스트")
	@Transactional
	@Nested
	class ReissueTest {
		@DisplayName("유효한 RefreshToken을 입력하면 토큰을 재발행한다")
		@Test
		void success() throws Exception {
			// given
			User user = userJPARepository.findByEmail(TEST_USER_EMAIL)
				.orElseThrow(() -> new Exception404(UserExceptionStatus.USER_NOT_FOUND));

			String refreshToken = JwtProvider.createRefreshToken(user);
			refreshTokenRepository.save(new RefreshToken(refreshToken, user.getUserId(), user.getEmail(), null));
			ReissueDto.ReissueRequestDto requestDto =
				ReissueDto.ReissueRequestDto.builder().refreshToken(refreshToken).build();

			String requestBody = om.writeValueAsString(requestDto);

			// when
			ResultActions resultActions = mvc.perform(
				RestDocumentationRequestBuilders.post("/api/auth/reissue")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody)
			);
			String responseBody = resultActions.andReturn().getResponse().getContentAsString();

			// Nested한 객체는 아래와 같이 Generic의 경우 .class가 안되는 이유로 TypeReference를 통해서 가져와야한다.
			ApiUtils.ApiResult<ReissueDto.ReissueResponseDto> resultDto =
				om.readValue(responseBody, new TypeReference<ApiUtils.ApiResult<ReissueDto.ReissueResponseDto>>() {
				});

			// then
			assertFalse(refreshTokenRepository.existsById(refreshToken));
			assertTrue(refreshTokenRepository.existsById(resultDto.response().refreshToken()));

			resultActions.andExpectAll(
				jsonPath("$.success").value("true"),
				jsonPath("$.response.accessToken").isString(),
				jsonPath("$.response.refreshToken").isString(),
				jsonPath("$.error").doesNotExist()
			);
		}
	}

	@DisplayName("로그아웃 통합 테스트")
	@Transactional
	@WithUserDetails(value = TEST_USER_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Nested
	class LogoutTest {
		@DisplayName("로그인이 된 회원은 로그아웃을 할 수 있다")
		@Test
		void success() throws Exception {
			// given
			User user = userJPARepository.findByEmail(TEST_USER_EMAIL)
				.orElseThrow(() -> new Exception404(UserExceptionStatus.USER_NOT_FOUND));

			String accessToken = JwtProvider.create(user);
			String refreshToken = JwtProvider.createRefreshToken(user);
			refreshTokenRepository.save(new RefreshToken(refreshToken, user.getUserId(), user.getEmail(), accessToken));

			// when
			ResultActions resultActions = mvc.perform(
				RestDocumentationRequestBuilders.post("/api/auth/logout")
					.header(JwtProvider.HEADER, accessToken)
			);

			// then
			assertFalse(refreshTokenRepository.existsById(refreshToken));
			assertTrue(blackListTokenRepository.existsById(accessToken));

			resultActions.andExpectAll(
				jsonPath("$.success").value("true"),
				jsonPath("$.response").doesNotExist(),
				jsonPath("$.error").doesNotExist()
			);
		}
	}

	@DisplayName("회원 탈퇴 통합 테스트")
	@Transactional
	@WithUserDetails(value = TEST_USER_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Nested
	class withdrawalTest {
		@DisplayName("디비에 회원이 있다면 회원탈퇴를 한다")
		@Test
		void success() throws Exception {
			// given
			User user = userJPARepository.findByEmail(TEST_USER_EMAIL)
				.orElseThrow(() -> new Exception404(UserExceptionStatus.USER_NOT_FOUND));

			String accessToken = JwtProvider.create(user);
			String refreshToken = JwtProvider.createRefreshToken(user);
			refreshTokenRepository.save(new RefreshToken(refreshToken, user.getUserId(), user.getEmail(), accessToken));

			// when
			ResultActions resultActions = mvc.perform(
				RestDocumentationRequestBuilders.post("/api/auth/withdrawal")
					.header(JwtProvider.HEADER, accessToken)
			);

			// then
			assertTrue(userJPARepository.findByEmail(TEST_USER_EMAIL).isEmpty());
			assertFalse(refreshTokenRepository.existsById(refreshToken));
			assertTrue(blackListTokenRepository.existsById(accessToken));

			resultActions.andExpectAll(
				jsonPath("$.success").value("true"),
				jsonPath("$.response").doesNotExist(),
				jsonPath("$.error").doesNotExist()
			);
		}
	}
}
