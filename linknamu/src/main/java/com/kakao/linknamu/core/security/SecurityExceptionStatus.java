package com.kakao.linknamu.core.security;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SecurityExceptionStatus implements BaseExceptionStatus {
    UNAUTHORIZED("인증되지 않았습니다.", 401, 14010),
    FORBIDDEN("권한이 없습니다.", 403, 14030);

    @Getter
    private final String message;
    @Getter
    private final int status;
    @Getter
    private final int errorCode;
}
