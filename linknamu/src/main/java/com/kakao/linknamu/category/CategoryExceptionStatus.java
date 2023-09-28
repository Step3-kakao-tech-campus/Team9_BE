package com.kakao.linknamu.category;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryExceptionStatus implements BaseExceptionStatus {
    CATEGORY_NOT_FOUND("카테고리가 존재하지 않습니다.", 404),
    CATEGORY_FORBIDDEN("카테고리에 접근할 수 없습니다.", 403);

    @Getter
    private final String message;
    @Getter
    private final int status;
}
