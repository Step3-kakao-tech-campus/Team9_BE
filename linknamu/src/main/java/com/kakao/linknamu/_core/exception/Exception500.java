package com.kakao.linknamu._core.exception;

import com.kakao.linknamu._core.BaseStatus;
import com.kakao.linknamu._core.util.ApiUtils;
import org.springframework.http.HttpStatus;

public class Exception500 extends ServerException{
    public Exception500(BaseStatus exception) {
        super(exception.getMessage());
    }

    @Override
    public ApiUtils.ApiResult<?> body() {return ApiUtils.error(getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}

    @Override
    public HttpStatus status() { return HttpStatus.INTERNAL_SERVER_ERROR;}
}
