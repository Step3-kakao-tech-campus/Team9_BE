package com.kakao.linknamu.redis.repository;

import com.kakao.linknamu._core.RedisContainerExtension;
import com.kakao.linknamu._core.redis.entity.RefreshToken;
import com.kakao.linknamu._core.redis.repository.RefreshTokenRepository;
import com.kakao.linknamu._core.security.JwtProvider;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({RedisContainerExtension.class, SpringExtension.class})
public class RefreshTokenRepositoryTest{
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenRepositoryTest(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @DisplayName("Refresh 토큰으로 레디스 데이터를 조회할 수 있다")
    @Test
    void findByIdTest() {
        //given
        User user = User.builder().userId(1L)
                .email("rjsdnxogh@naver.com")
                .provider(Provider.PROVIDER_GOOGLE)
                .role(Role.ROLE_USER)
                .build();
        String accessToken = JwtProvider.create(user);
        String refreshToken = JwtProvider.createRefreshToken(user);
        RefreshToken refreshTokenEntity = RefreshToken.of(refreshToken, accessToken, user);

        refreshTokenRepository.save(refreshTokenEntity);

        //when
        RefreshToken findByrefreshToken = refreshTokenRepository.findById(refreshToken).get();

        //then
        assertEquals(refreshTokenEntity, findByrefreshToken);
    }

    @DisplayName("레디스에 Refresh 토큰이 없다면 null을 반환한다")
    @Test
    void findByIdNullTest() {
        //given
        User user = User.builder().userId(1L)
                .email("rjsdnxogh@naver.com")
                .provider(Provider.PROVIDER_GOOGLE)
                .role(Role.ROLE_USER)
                .build();
        String accessToken = JwtProvider.create(user);
        String refreshToken = JwtProvider.createRefreshToken(user);

        //when
        Optional<RefreshToken> findByrefreshToken = refreshTokenRepository.findById(refreshToken);

        //then
        assertTrue(findByrefreshToken.isEmpty());
    }

    @DisplayName("Access 토큰으로 레디스 데이터를 조회할 수 있다")
    @Test
    void findByAccessTokenTest() {
        //given
        User user = User.builder().userId(1L)
                .email("rjsdnxogh@naver.com")
                .provider(Provider.PROVIDER_GOOGLE)
                .role(Role.ROLE_USER)
                .build();
        String accessToken = JwtProvider.create(user);
        String refreshToken = JwtProvider.createRefreshToken(user);
        RefreshToken refreshTokenEntity = RefreshToken.of(refreshToken, accessToken, user);

        refreshTokenRepository.save(refreshTokenEntity);

        //when
        RefreshToken findByrefreshToken = refreshTokenRepository.findByAccessToken(accessToken).get();

        //then
        assertEquals(refreshTokenEntity, findByrefreshToken);
    }

    @DisplayName("레디스에 Access 토큰이 없다면 null을 반환한다")
    @Test
    void findByAccessTokenNullTest() {
        //given
        User user = User.builder().userId(1L)
                .email("rjsdnxogh@naver.com")
                .provider(Provider.PROVIDER_GOOGLE)
                .role(Role.ROLE_USER)
                .build();
        String accessToken = JwtProvider.create(user);
        String refreshToken = JwtProvider.createRefreshToken(user);

        //when
        Optional<RefreshToken> findByrefreshToken = refreshTokenRepository.findByAccessToken(accessToken);

        //then
        assertTrue(findByrefreshToken.isEmpty());
    }
}
