package com.kakao.linknamu.googleDocs;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GoogleDocsExceptionStatus implements BaseExceptionStatus {
	DOCS_ALREADY_EXIST("이미 구글 독스 링크 연동 페이지가 존재합니다.", 400),
	DOCS_FORBIDDEN("권한이 없는 접근입니다.", 403),
	DOCS_NOT_FOUND("존재하지 않는 데이터입니다.", 404);

	@Getter
	private final String message;
	@Getter
	private final int status;
}
