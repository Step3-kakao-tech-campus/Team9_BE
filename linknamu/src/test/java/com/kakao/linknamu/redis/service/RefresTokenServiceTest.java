package com.kakao.linknamu.redis.service;

import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu._core.redis.RedisExceptionStatus;
import com.kakao.linknamu._core.redis.entity.RefreshToken;
import com.kakao.linknamu._core.redis.repository.RefreshTokenRepository;
import com.kakao.linknamu._core.redis.service.RefreshTokenService;
import com.kakao.linknamu._core.security.JwtProvider;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RefresTokenServiceTest {
    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setup() {
        JwtProvider.ACCESS_SECRET = UUID.randomUUID().toString();
        JwtProvider.REFRESH_SECRET = UUID.randomUUID().toString();
    }

    @DisplayName("RefreshToken 저장 테스트")
    @Nested
    class SaveTest {
        @DisplayName("Refresh 토큰, Access 토큰, 회원 정보를 입력하면 저장이 된다")
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
            String refreshToken = JwtProvider.createRefreshToken(user);
            RefreshToken refreshTokenEntity = RefreshToken.of(refreshToken, accessToken, user);
            ArgumentCaptor<RefreshToken> tokenArgumentCaptor = ArgumentCaptor.forClass(RefreshToken.class);

            // mock
            given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(refreshTokenEntity);

            // when
            refreshTokenService.save(refreshToken, accessToken, user);

            // then
            verify(refreshTokenRepository).save(tokenArgumentCaptor.capture());

            assertEquals(refreshTokenEntity, tokenArgumentCaptor.getValue());
        }
    }

    @DisplayName("RefreshToken ID로 삭제 테스트")
    @Nested
    class DeleteByIdTest {
        @DisplayName("Refresh 토큰을 통해서 삭제할 수 있다")
        @Test
        void success() {
            // given
            User user = User.builder()
                    .userId(1L)
                    .email("rjsdnxogh55@gmail.com")
                    .role(Role.ROLE_USER)
                    .provider(Provider.PROVIDER_GOOGLE)
                    .build();
            String refreshToken = JwtProvider.createRefreshToken(user);
            // mock
            willDoNothing().given(refreshTokenRepository).deleteById(eq(refreshToken));

            // when
            refreshTokenService.deleteById(refreshToken);

            // then
            verify(refreshTokenRepository, times(1)).deleteById(refreshToken);
        }
    }

    @DisplayName("AccessToken으로 삭제 테스트")
    @Nested
    class DeleteByAccessTokenTest {
        @DisplayName("AccessToken 토큰을 통해서 삭제할 수 있다")
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
            String refreshToken = JwtProvider.createRefreshToken(user);
            RefreshToken refreshTokenEntity = RefreshToken.of(refreshToken, accessToken, user);
            ArgumentCaptor<String> captorRefreshToken = ArgumentCaptor.forClass(String.class);
            // mock
            given(refreshTokenRepository.findByAccessToken(eq(accessToken)))
                    .willReturn(Optional.of(refreshTokenEntity));
            willDoNothing().given(refreshTokenRepository).deleteById(refreshToken);

            // when
            refreshTokenService.deleteByAccessToken(accessToken);

            // then
            verify(refreshTokenRepository, times(1)).deleteById(captorRefreshToken.capture());
            assertEquals(refreshToken, captorRefreshToken.getValue());
        }

        @DisplayName("AccessToken 토큰이 없다면 예외를 발생시킨다")
        @Test
        void failNoAccessTokenInRedis() {
            // given
            User user = User.builder()
                    .userId(1L)
                    .email("rjsdnxogh55@gmail.com")
                    .role(Role.ROLE_USER)
                    .provider(Provider.PROVIDER_GOOGLE)
                    .build();
            String accessToken = JwtProvider.create(user);

            // mock
            given(refreshTokenRepository.findByAccessToken(eq(accessToken)))
                    .willReturn(Optional.empty());

            // when
            Throwable exception = assertThrows(Exception404.class,
                    () -> refreshTokenService.deleteByAccessToken(accessToken));

            // then
            assertEquals(RedisExceptionStatus.REFRESH_TOKEN_NOT_FOUND.getMessage(), exception.getMessage());
        }
    }

    @DisplayName("RefreshToken ID로 존재여부 확인 테스트")
    @Nested
    class ExistByIdTest{
        @DisplayName("RefreshToken이 존재한다면 참을 반환한다.")
        @Test
        void successTrue() {
            // given
            User user = User.builder()
                    .userId(1L)
                    .email("rjsdnxogh55@gmail.com")
                    .role(Role.ROLE_USER)
                    .provider(Provider.PROVIDER_GOOGLE)
                    .build();
            String refreshToken = JwtProvider.createRefreshToken(user);
            RefreshToken refreshTokenEntity = RefreshToken.of(refreshToken, "12313", user);

            // mock
            given(refreshTokenRepository.existsById(eq(refreshToken))).willReturn(true);

            // when
            boolean result = refreshTokenService.existsById(refreshToken);

            // then
            assertTrue(result);
        }

        @DisplayName("RefreshToken이 존재하지 않는다면 거짓을 반환한다.")
        @Test
        void successFalse() {
            // given
            User user = User.builder()
                    .userId(1L)
                    .email("rjsdnxogh55@gmail.com")
                    .role(Role.ROLE_USER)
                    .provider(Provider.PROVIDER_GOOGLE)
                    .build();
            String refreshToken = JwtProvider.createRefreshToken(user);
            RefreshToken refreshTokenEntity = RefreshToken.of(refreshToken, "12313", user);

            // mock
            given(refreshTokenRepository.existsById(eq(refreshToken))).willReturn(false);

            // when
            boolean result = refreshTokenService.existsById(refreshToken);

            // then
            assertFalse(result);
        }
    }
}
