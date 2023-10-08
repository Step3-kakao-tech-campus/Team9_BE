package com.kakao.linknamu.tag;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TagExceptionStatus implements BaseExceptionStatus {

    TAG_NOT_FOUND("대상 태그가 존재하지 않습니다.", 404);

    @Getter
    private final String message;
    @Getter
    private final int status;
}
