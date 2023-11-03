package com.kakao.linknamu.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.core.redis.service.BlackListTokenService;
import com.kakao.linknamu.core.redis.service.RefreshTokenService;
import com.kakao.linknamu.core.security.JwtProvider;
import com.kakao.linknamu.user.UserExceptionStatus;
import com.kakao.linknamu.user.dto.LoginResponseDto;
import com.kakao.linknamu.user.dto.ReissueDto;
import com.kakao.linknamu.user.dto.oauth.GoogleUserInfo;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJPARepository;
import com.kakao.linknamu.workspace.service.WorkspaceSaveService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserService userService;
	@Spy
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Mock
	private UserJPARepository userJPARepository;
	@Mock
	private RefreshTokenService refreshTokenService;
	@Mock
	private BlackListTokenService blackListTokenService;
	@Mock
	private WorkspaceSaveService workspaceSaveService;

	@BeforeEach
	public void setUp() {
		JwtProvider.ACCESS_SECRET = UUID.randomUUID().toString();
		JwtProvider.REFRESH_SECRET = UUID.randomUUID().toString();
	}

	@DisplayName("소셜 로그인 테스트")
	@Nested
	class SocialLoginTest {
		@DisplayName("기존의 회원이 없다면 회원가입을 한 후 토큰을 발행한다")
		@Test
		void successWithSignUp() {
			// given
			GoogleUserInfo googleUserInfo = new GoogleUserInfo("123", "rjsdnxogh55@gmail.com", true, null);
			User user = User.builder()
				.userId(1L)
				.email(googleUserInfo.email())
				.role(Role.ROLE_USER)
				.provider(Provider.PROVIDER_GOOGLE)
				.password(passwordEncoder.encode(UUID.randomUUID().toString()))
				.build();
			ArgumentCaptor<String> workspaceNameCaptor = ArgumentCaptor.forClass(String.class);

			// mock
			given(userJPARepository.findByEmail(eq(googleUserInfo.email()))).willReturn(Optional.empty());
			given(userJPARepository.save(any())).willReturn(user);
			given(workspaceSaveService.createWorkspace(any(), any())).willReturn(null);

			// when
			LoginResponseDto result = userService.socialLogin(googleUserInfo);

			// then
			verify(userJPARepository, times(1)).save(any());
			verify(workspaceSaveService, times(1)).createWorkspace(workspaceNameCaptor.capture(), any());
			assertEquals("나의 워크스페이스", workspaceNameCaptor.getValue());
			assertTrue(result.accessToken().startsWith("Bearer "));
			JwtProvider.verifyRefreshToken(result.refreshToken());
		}

		@DisplayName("기존의 회원이 있다면 바로 토큰을 발행한다")
		@Test
		void successWithoutSignUp() {
			// given
			GoogleUserInfo googleUserInfo = new GoogleUserInfo("123", "rjsdnxogh55@gmail.com", true, null);
			User user = User.builder()
				.userId(1L)
				.email(googleUserInfo.email())
				.role(Role.ROLE_USER)
				.provider(Provider.PROVIDER_GOOGLE)
				.password(passwordEncoder.encode(UUID.randomUUID().toString()))
				.build();

			// mock
			given(userJPARepository.findByEmail(eq(googleUserInfo.email()))).willReturn(Optional.of(user));

			// when
			LoginResponseDto result = userService.socialLogin(googleUserInfo);

			// then
			assertTrue(result.accessToken().startsWith("Bearer "));
			JwtProvider.verifyRefreshToken(result.refreshToken());
		}
	}

	@DisplayName("토큰 재발행 테스트")
	@Nested
	class TokenReissueTest {
		@DisplayName("Refresh Token을 주면 새로운 Access, Refresh 토큰을 발행한다.")
		@Test
		void success() {
			// given
			User user = User.builder()
				.userId(1L)
				.email("rjsdnxogh55@gmail.com")
				.role(Role.ROLE_USER)
				.provider(Provider.PROVIDER_GOOGLE)
				.password(passwordEncoder.encode(UUID.randomUUID().toString()))
				.build();
			String refreshToken = JwtProvider.createRefreshToken(user);
			ReissueDto.ReissueRequestDto requestDto = new ReissueDto.ReissueRequestDto(refreshToken);
			ArgumentCaptor<String> deleteRefreshTokenCapture = ArgumentCaptor.forClass(String.class);
			ArgumentCaptor<User> saveUserCapture = ArgumentCaptor.forClass(User.class);

			// mock
			given(refreshTokenService.existsById(eq(refreshToken))).willReturn(true);
			willDoNothing().given(refreshTokenService).deleteById(eq(refreshToken));
			willDoNothing().given(refreshTokenService).save(any(), any(), any());

			// when
			ReissueDto.ReissueResponseDto result = userService.reissue(requestDto);

			// then
			verify(refreshTokenService).deleteById(deleteRefreshTokenCapture.capture());
			verify(refreshTokenService).save(any(), any(), saveUserCapture.capture());

			assertEquals(refreshToken, deleteRefreshTokenCapture.getValue());
			assertEquals(user, saveUserCapture.getValue());
			assertTrue(result.accessToken().startsWith("Bearer "));
			JwtProvider.verifyRefreshToken(result.refreshToken());
		}

		@DisplayName("유효하지 않은 RefreshToken을 입력하면 예외를 발생시킨다")
		@Test
		void failNotInvalidRefreshToken() {
			// given
			String refreshToken = "Invalid Token!";
			ReissueDto.ReissueRequestDto requestDto = new ReissueDto.ReissueRequestDto(refreshToken);

			// when
			Throwable exception = assertThrows(Exception400.class, () -> userService.reissue(requestDto));

			// then
			assertEquals(UserExceptionStatus.REFRESH_TOKEN_INVALID.getMessage(), exception.getMessage());
		}
	}

	@DisplayName("로그아웃 테스트")
	@Nested
	class LogoutTest {
		@DisplayName("로그인한 사용자는 로그아웃을 할 수 있다")
		@Test
		void success() {
			// given
			User user = User.builder()
				.userId(1L)
				.email("rjsdnxogh55@gmail.com")
				.role(Role.ROLE_USER)
				.provider(Provider.PROVIDER_GOOGLE)
				.password(passwordEncoder.encode(UUID.randomUUID().toString()))
				.build();
			String accessToken = JwtProvider.create(user);

			// mock
			willDoNothing().given(refreshTokenService).deleteByAccessToken(eq(accessToken));
			willDoNothing().given(blackListTokenService).save(eq(accessToken));
			// when
			userService.logout(accessToken);

			// then
			verify(refreshTokenService, times(1)).deleteByAccessToken(eq(accessToken));
			verify(blackListTokenService, times(1)).save(eq(accessToken));
		}
	}

	@DisplayName("회원 탈퇴 테스트")
	@Nested
	class WithdrawalTest {
		@DisplayName("디비에 회원이 있는 경우 회원을 삭제한다.")
		@Test
		void success() {
			// given
			User user = User.builder()
				.userId(1L)
				.email("rjsdnxogh55@gmail.com")
				.role(Role.ROLE_USER)
				.provider(Provider.PROVIDER_GOOGLE)
				.password(passwordEncoder.encode(UUID.randomUUID().toString()))
				.build();
			String accessToken = JwtProvider.create(user);

			// mock
			given(userJPARepository.findById(eq(user.getUserId()))).willReturn(Optional.of(user));
			willDoNothing().given(userJPARepository).delete(eq(user));
			willDoNothing().given(refreshTokenService).deleteByAccessToken(eq(accessToken));
			willDoNothing().given(blackListTokenService).save(eq(accessToken));

			// when
			userService.withdrawal(user, accessToken);

			// then
			verify(userJPARepository, times(1)).delete(eq(user));
			verify(refreshTokenService, times(1)).deleteByAccessToken(eq(accessToken));
			verify(blackListTokenService, times(1)).save(eq(accessToken));
		}

		@DisplayName("디비에 회원이 없는 경우 예외를 발생시킨다")
		@Test
		void failUserNotInDB() {
			// given
			User user = User.builder()
				.userId(1L)
				.email("rjsdnxogh55@gmail.com")
				.role(Role.ROLE_USER)
				.provider(Provider.PROVIDER_GOOGLE)
				.password(passwordEncoder.encode(UUID.randomUUID().toString()))
				.build();
			String accessToken = JwtProvider.create(user);

			// mock
			given(userJPARepository.findById(eq(user.getUserId()))).willReturn(Optional.empty());

			// when
			Throwable exception = assertThrows(Exception404.class,
				() -> userService.withdrawal(user, accessToken));

			// then
			assertEquals(UserExceptionStatus.USER_NOT_FOUND.getMessage(), exception.getMessage());
		}
	}
}
