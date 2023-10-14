package com.kakao.linknamu.googleDocs;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GoogleDocsExceptionStatus implements BaseExceptionStatus {
    DOCS_ALREADY_EXIST("이미 구글 독스 링크 연동 페이지가 존재합니다.", 400);

    @Getter
    private final String message;
    @Getter
    private final int status;
}
