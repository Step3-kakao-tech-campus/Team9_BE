package com.kakao.linknamu.bookmark;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkExceptionStatus implements BaseExceptionStatus {
    BOOKMARK_NOT_FOUND("대상 북마크가 존재하지 않습니다.", 404);

    @Getter
    private final String message;
    @Getter
    private final int status;
}
