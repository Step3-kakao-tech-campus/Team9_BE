package com.kakao.linknamu.user;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum UserExceptionStatus implements BaseExceptionStatus {
    USER_NOT_FOUND("회원이 존재하지 않습니다", 404),
    GOOGLE_TOKEN_MISSING("구글 accessToken을 입력해주세요.", 401),
    GOOGLE_TOKEN_INVALID("유효하지 않은 구글 accessToken입니다.", 400),
    GOOGLE_API_CONNECTION_ERROR("구글 API 연동 중 문제가 발생했습니다", 500);

    @Getter
    private final String message;
    @Getter
    private final int status;

}