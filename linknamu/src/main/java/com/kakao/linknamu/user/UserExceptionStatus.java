package com.kakao.linknamu.user;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum UserExceptionStatus implements BaseExceptionStatus {
    USER_NOT_FOUND("회원이 존재하지 않습니다", 404);


    @Getter
    private final String message;
    @Getter
    private final int status;

}
