package com.kakao.linknamu.core.exception;

import com.kakao.linknamu.core.util.ApiUtils;
import org.springframework.http.HttpStatus;

public class Exception403 extends ClientException {
	private final BaseExceptionStatus exceptionStatus;

	public Exception403(BaseExceptionStatus exception) {
		super(exception.getMessage());
		exceptionStatus = exception;

	}

	@Override
	public ApiUtils.ApiResult<?> body() {
		return ApiUtils.error(getMessage(), HttpStatus.FORBIDDEN.value(), exceptionStatus.getErrorCode());
	}

	@Override
	public HttpStatus status() {
		return HttpStatus.UNAUTHORIZED;
	}
}
