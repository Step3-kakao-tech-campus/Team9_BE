package com.kakao.linknamu._core.encryption;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EncryptionExceptionStatus implements BaseExceptionStatus {
    ENCRYPTION_INVALID_KEY("유효하지 않은 암호키 입니다.", 400),
    ENCRYPTION_SERVER_ERROR("서버에서 에러가 발생했습니다.", 500);

    private final String message;
    private final int status;
}
