package com.kakao.linknamu._core.redis;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RedisExceptionStatus implements BaseExceptionStatus {
    REFRESH_TOKEN_NOT_FOUND("Redis에 존재하지 않는 Refresh토큰입니다.", 404);


    @Getter
    private final String message;
    @Getter
    private final int status;
}
