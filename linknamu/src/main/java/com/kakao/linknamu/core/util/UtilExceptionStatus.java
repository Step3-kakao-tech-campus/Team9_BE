package com.kakao.linknamu.core.util;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UtilExceptionStatus implements BaseExceptionStatus {
	NOT_BASE64_DATA("Base64 형식이 아닙니다.", 400, "04004"),
	IMAGE_INVALID_DATA("유효하지 않은 이미지 데이터입니다.", 400, "04005"),

	IMAGE_UNREADABLE_DATA("이미지가 손상되었거나 읽을 수 없는 형식입니다.", 400, "04006"),

	IMAGE_URL_INVALID("유요하지 않는 이미지 링크입니다.", 400, "04007"),

	IMAGE_UNREADABLE_URL("URL 형식이 아닙니다.", 400, "04008");

	@Getter
	private final String message;
	@Getter
	private final int status;
	@Getter
	private final String errorCode;
}
