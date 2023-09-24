package com.kakao.linknamu._core.exception;

import com.kakao.linknamu._core.BaseStatus;
import com.kakao.linknamu._core.util.ApiUtils;
import org.springframework.http.HttpStatus;

public class Exception404 extends ClientException{
    public Exception404(BaseStatus exception) {
        super(exception.getMessage());
    }

    @Override
    public ApiUtils.ApiResult<?> body() {return ApiUtils.error(getMessage(), HttpStatus.NOT_FOUND);}

    @Override
    public HttpStatus status() { return HttpStatus.UNAUTHORIZED;}
}
