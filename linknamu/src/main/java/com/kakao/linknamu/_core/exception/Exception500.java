package com.kakao.linknamu._core.exception;

import com.kakao.linknamu._core.util.ApiUtils;
import org.springframework.http.HttpStatus;

public class Exception500 extends ServerException{
    private final BaseExceptionStatus exceptionStatus;

    public Exception500(BaseExceptionStatus exception) {
        super(exception.getMessage());
        exceptionStatus = exception;
    }

    @Override
    public ApiUtils.ApiResult<?> body() {return ApiUtils.error(getMessage(), exceptionStatus.getStatus());}

    @Override
    public HttpStatus status() { return HttpStatus.INTERNAL_SERVER_ERROR;}
}
