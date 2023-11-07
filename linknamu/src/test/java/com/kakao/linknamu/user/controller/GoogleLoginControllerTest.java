package com.kakao.linknamu.user.controller;

import com.kakao.linknamu.core.RedisContainerExtension;
import com.kakao.linknamu.core.RestDocs;
import com.kakao.linknamu.user.dto.oauth.GoogleTokenResponseDto;
import com.kakao.linknamu.user.dto.oauth.GoogleUserInfo;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJpaRepository;
import com.kakao.linknamu.workspace.repository.WorkspaceJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(RedisContainerExtension.class)
public class GoogleLoginControllerTest extends RestDocs {

	@Autowired
	private UserJpaRepository userJPARepository;
	@Autowired
	private WorkspaceJpaRepository workspaceJPARepository;
	@MockBean
	private RestTemplate restTemplate;

	@DisplayName("구글 소셜 로그인 통합 테스트")
	@Transactional
	@Nested
	class GoogleSocialLoginTest {
		@DisplayName("회원가입이 되어있지 않다면, 회원가입을 한 뒤 로그인을 한다")
		@Test
		void successNoUserInDB() throws Exception {
			// given
			GoogleUserInfo googleUserInfo = new GoogleUserInfo("123", "rjsdnxogh55@gmail.com",
				true, null);
			String googleToken = "googleAccessToken";


			// mock
			given(
				restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(GoogleUserInfo.class)))
				.willReturn(ResponseEntity.ok((googleUserInfo)));


			// when
			ResultActions resultActions = mvc.perform(
				RestDocumentationRequestBuilders.post("/api/auth/google/login")
					.header("Google", googleToken));

			// then
			resultActions.andExpectAll(
				jsonPath("$.success").value("true"),
				jsonPath("$.response.accessToken").isString(),
				jsonPath("$.response.refreshToken").isString(),
				jsonPath("$.error").doesNotExist()
			);
			assertEquals(1, userJPARepository.count());
			assertEquals(1, workspaceJPARepository.count());
		}

		@DisplayName("회원가입이 되어있다면, 바로 로그인을 한다")
		@Test
		void successUserInDB() throws Exception {
			// given
			User user = User.builder()
				.email("rjsdnxogh55@gmail.com")
				.role(Role.ROLE_USER)
				.provider(Provider.PROVIDER_GOOGLE)
				.password("test123")
				.build();
			GoogleUserInfo googleUserInfo = new GoogleUserInfo("123", "rjsdnxogh55@gmail.com",
				true, null);
			String googleToken = "googleAccessToken";


			// mock
			given(
				restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(GoogleUserInfo.class)))
				.willReturn(ResponseEntity.ok((googleUserInfo)));


			// when
			ResultActions resultActions = mvc.perform(
				RestDocumentationRequestBuilders.post("/api/auth/google/login")
					.header("Google", googleToken));

			// then
			resultActions.andExpectAll(
				jsonPath("$.success").value("true"),
				jsonPath("$.response.accessToken").isString(),
				jsonPath("$.response.refreshToken").isString(),
				jsonPath("$.error").doesNotExist()
			);
			assertEquals(1, userJPARepository.count());
		}
	}
}
