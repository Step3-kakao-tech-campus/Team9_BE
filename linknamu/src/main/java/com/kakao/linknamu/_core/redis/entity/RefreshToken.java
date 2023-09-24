package com.kakao.linknamu._core.redis.entity;

import com.kakao.linknamu._core.security.JwtProvider;
import com.kakao.linknamu.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash(value = "refreshToken")
public class RefreshToken {
    // Jakarta(하이버네이트) Id가 아닌 Springframework Id를 사용해야 합니다.
    @Id
    private String refreshToken;
    private Long userId;
    private String email;

    @Indexed
    private String accessToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiraition = JwtProvider.REFRESH_EXP;

    @Builder
    public RefreshToken(String refreshToken, Long userId, String email, String accessToken) {
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
        this.accessToken = accessToken;
    }

    public static RefreshToken of(String refreshToken, String accessToken, User user) {
        return RefreshToken.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .userId(user.getUserId())
                .email(user.getEmail())
                .build();
    }
}
