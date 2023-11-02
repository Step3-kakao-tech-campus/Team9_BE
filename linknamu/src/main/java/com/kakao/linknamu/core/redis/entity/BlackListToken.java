package com.kakao.linknamu.core.redis.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RedisHash(value = "BlackList")
@Getter
public class BlackListToken {
    @Id
    private String accessToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration;

    @Builder
    public BlackListToken(String accessToken, Long expiration) {
        this.accessToken = accessToken;
        this.expiration = expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlackListToken that = (BlackListToken) o;
        return Objects.equals(getAccessToken(), that.getAccessToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccessToken());
    }
}
