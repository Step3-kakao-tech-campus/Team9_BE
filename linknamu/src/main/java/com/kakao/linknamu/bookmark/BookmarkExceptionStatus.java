package com.kakao.linknamu.bookmark;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkExceptionStatus implements BaseExceptionStatus {
	BOOKMARK_ALREADY_EXISTS("해당 북마크 링크가 이미 존재합니다.", 400, "24000"),
	BOOKMARK_FORBIDDEN("북마크 접근 권한이 없는 사용자입니다.", 403, "24030"),
	BOOKMARK_NOT_FOUND("대상 북마크가 존재하지 않습니다.", 404, "24040");

	@Getter
	private final String message;
	@Getter
	private final int status;
	@Getter
	private final String errorCode;
}
