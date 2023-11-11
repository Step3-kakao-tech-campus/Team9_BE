package com.kakao.linknamu.bookmarktag;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkTagExceptionStatus implements BaseExceptionStatus {
	BOOKMARK_TAG_DUPLICATE("데이터가 이미 존재합니다.", 400, "44000"),
	BOOKMARK_TAG_FORBIDDEN("권한이 없는 접근입니다.", 403, "44030"),
	BOOKMARK_TAG_NOT_FOUND("대상이 존재하지 않습니다.", 404, "44040");

	@Getter
	private final String message;
	@Getter
	private final int status;
	@Getter
	private final String errorCode;
}
