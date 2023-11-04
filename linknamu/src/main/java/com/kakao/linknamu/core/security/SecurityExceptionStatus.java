package com.kakao.linknamu.core.security;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SecurityExceptionStatus implements BaseExceptionStatus {
    UNAUTHORIZED("인증되지 않았습니다.", 401, "04010"),
    FORBIDDEN("권한이 없습니다.", 403, "04030");

    @Getter
    private final String message;
    @Getter
    private final int status;
    @Getter
    private final String errorCode;
}
