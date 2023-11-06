package com.kakao.linknamu.thirdparty.googleDocs;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GoogleDocsExceptionStatus implements BaseExceptionStatus {
    DOCS_ALREADY_EXIST("이미 구글 독스 링크 연동 페이지가 존재합니다.", 400, "74000"),
	GOOGLE_DOCS_NOT_ACCESS("구글 독스에 접근 권한이 없습니다. 문서의 접근 권한을 변경해주세요.", 403, "74030"),
    DOCS_FORBIDDEN("권한이 없는 접근입니다.", 403, "74031"),
	GOOGLE_DOCS_NOT_EXIST("없는 구글 독스입니다.", 404, "74040"),
	DOCS_NOT_FOUND("존재하지 않는 데이터입니다.", 404, "74041"),
    GOOGLE_DOCS_LINK_ERROR("구글 독스 연동 중 문제가 발생했습니다.", 500, "75000");

	@Getter
	private final String message;
	@Getter
	private final int status;
	@Getter
	private final String errorCode;
}
