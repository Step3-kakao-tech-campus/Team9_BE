package com.kakao.linknamu._core.redis.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "BlackList")
@Getter
public class BlackListToken {
    @Id
    private String accessToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiraition;

    @Builder
    public BlackListToken(String accessToken, Long expiraition) {
        this.accessToken = accessToken;
        this.expiraition = expiraition;
    }
}
