package com.kakao.linknamu.bookmarkTag;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkTagExceptionStatus implements BaseExceptionStatus {
	BOOKMARK_TAG_DUPLICATE("데이터가 이미 존재합니다.", 400, 54000),
	BOOKMARK_TAG_FORBIDDEN("권한이 없는 접근입니다.", 403, 54030),
	BOOKMARK_TAG_NOT_FOUND("대상이 존재하지 않습니다.", 404, 54040);

	@Getter
	private final String message;
	@Getter
	private final int status;
	@Getter
	private final int errorCode;
}
