package com.kakao.linknamu.thirdparty.notion;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotionExceptionStatus implements BaseExceptionStatus {
	INVALID_NOTION_CODE("유효하지 않은 노션 코드입니다.", 400, "64000"),
	INVALID_NOTION_PAGE_AND_AUTHORIZATION("존재하지 않은 페이지 혹은 권한이 없는 접근입니다.", 400, "64001"),
	NOTION_ALREADY_EXIST("이미 존재하는 데이터입니다.", 400, "64002"),
	INVALID_NOTION_PAGE_ID("페이지 ID형식에 맞지 않는 입력입니다.", 400, "64003"),
	NOTION_FORBIDDEN("권한이 없는 접근입니다.", 403, "64030"),
	NOTION_ACCOUNT_NOT_FOUND("존재하지 않는 데이터입니다.", 404, "64040"),
	NOTION_LINK_ERROR("노션 연동 중 문제가 발생했습니다.", 500, "65000");

	@Getter
	private final String message;
	@Getter
	private final int status;
	@Getter
	private final String errorCode;
}
