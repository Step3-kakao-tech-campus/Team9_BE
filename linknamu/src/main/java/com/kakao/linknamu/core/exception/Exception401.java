package com.kakao.linknamu.core.exception;

import com.kakao.linknamu.core.util.ApiUtils;
import org.springframework.http.HttpStatus;

public class Exception401 extends ClientException{
    private final BaseExceptionStatus exceptionStatus;

    public Exception401(BaseExceptionStatus exception) {
        super(exception.getMessage());
        exceptionStatus = exception;
    }

    @Override
    public ApiUtils.ApiResult<?> body() {return ApiUtils.error(getMessage(), exceptionStatus.getStatus(), exceptionStatus.getErrorCode());}

    @Override
    public HttpStatus status() { return HttpStatus.UNAUTHORIZED;}
}
