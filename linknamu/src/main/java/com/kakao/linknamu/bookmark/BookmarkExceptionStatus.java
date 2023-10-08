package com.kakao.linknamu.bookmark;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkExceptionStatus implements BaseExceptionStatus {
    BOOKMARK_NOT_FOUND("대상 북마크가 존재하지 않습니다.", 404),
    BOOKMARK_FORBIDDEN("북마크 접근 권한이 없는 사용자입니다.", 403),
    BOOKMARK_WRONG_REQUEST("북마크 소유자가 한 명이 아닙니다.", 400);

    @Getter
    private final String message;
    @Getter
    private final int status;
}
