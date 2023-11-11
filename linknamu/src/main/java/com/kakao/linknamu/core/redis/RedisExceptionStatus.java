package com.kakao.linknamu.core.redis;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RedisExceptionStatus implements BaseExceptionStatus {
	REFRESH_TOKEN_NOT_FOUND("존재하지 않는 Refresh토큰입니다.", 404, "04040"),
	BLACKLIST_TOKEN("블랙리스트에 존재하는 토큰입니다.", 403, "04031");

	@Getter
	private final String message;
	@Getter
	private final int status;
	@Getter
	private final String errorCode;
}
