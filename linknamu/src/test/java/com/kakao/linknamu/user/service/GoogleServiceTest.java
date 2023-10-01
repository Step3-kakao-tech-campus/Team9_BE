package com.kakao.linknamu.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu.user.UserExceptionStatus;
import com.kakao.linknamu.user.dto.oauth.GoogleUserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GoogleServiceTest {
    @InjectMocks
    private GoogleService googleService;

    @Spy
    private ObjectMapper om = new ObjectMapper();

    @Mock
    private RestTemplate restTemplate;

    @DisplayName("구글 유저 정보 받기 테스트")
    @Nested
    class GetGoogleUserInfoTest {
        @DisplayName("구글 AccessToken을 입력하면 구글 유저 정보를 반환한다")
        @Test
        void success() {
            // given
            GoogleUserInfo googleUserInfo = new GoogleUserInfo("123", "rjsdnxogh55@gmail.com",
                    true, null);
            String token = "anyString";


            // mock
            given(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(GoogleUserInfo.class)))
                    .willReturn(ResponseEntity.of(Optional.of(googleUserInfo)));

            // when
            GoogleUserInfo result = googleService.getGoogleUserInfo(token);

            // then
            assertEquals(googleUserInfo.email(), result.email());
        }

        @DisplayName("유효하지 않은 AccessToken을 입력하면 예외를 발생시킨다")
        @Test
        void failInvalidAccessToken() {
            // given
            GoogleUserInfo googleUserInfo = new GoogleUserInfo("123", "rjsdnxogh55@gmail.com",
                    true, null);
            String token = "Invalid Token";


            // mock
            given(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(GoogleUserInfo.class)))
                    .willThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰 에러"));

            // when
            Throwable exception = assertThrows(Exception400.class, () -> googleService.getGoogleUserInfo(token));

            // then
            assertEquals(UserExceptionStatus.GOOGLE_TOKEN_INVALID.getMessage(), exception.getMessage());
        }
    }
}
