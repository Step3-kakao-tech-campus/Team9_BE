package com.kakao.linknamu.kakao;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum KakaoExceptionStatus implements BaseExceptionStatus {

	FILE_INVALID_FORMAT("파일이 txt 또는 csv 형식이 아닙니다.", 400, "84000"),
	FILE_NOTFOUND("파일이 존재하지 않습니다.", 400, "84001"),
	FILE_READ_FAILED("파일을 읽는 과정에서 오류가 발생했습니다.", 500, "85000");

	@Getter
	private final String message;
	@Getter
	private final int status;
	@Getter
	private final String errorCode;
}
