package com.kakao.linknamu._core.exception;

import com.kakao.linknamu._core.util.ApiUtils;
import org.springframework.http.HttpStatus;

public class Exception403 extends ClientException{
    public Exception403(BaseExceptionStatus exception) {
        super(exception.getMessage());
    }

    @Override
    public ApiUtils.ApiResult<?> body() {return ApiUtils.error(getMessage(), HttpStatus.FORBIDDEN);}

    @Override
    public HttpStatus status() { return HttpStatus.UNAUTHORIZED;}
}
