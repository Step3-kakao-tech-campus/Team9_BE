package com.kakao.linknamu.notion;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum NotionExceptionStatus implements BaseExceptionStatus {
    NOTION_ALREADY_EXIST("이미 존재하는 데이터입니다.", 400);

    @Getter
    private final String message;
    @Getter
    private final int status;
}
