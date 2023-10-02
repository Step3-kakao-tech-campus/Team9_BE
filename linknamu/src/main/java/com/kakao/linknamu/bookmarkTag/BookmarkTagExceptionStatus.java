package com.kakao.linknamu.bookmarkTag;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkTagExceptionStatus implements BaseExceptionStatus {
    BOOKMARK_TAG_NOT_FOUND("대상이 존재하지 않습니다.", 404);

    @Getter
    private final String message;
    @Getter
    private final int status;
}
