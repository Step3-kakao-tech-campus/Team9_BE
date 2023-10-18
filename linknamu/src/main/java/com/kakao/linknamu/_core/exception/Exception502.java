package com.kakao.linknamu._core.exception;

import com.kakao.linknamu._core.util.ApiUtils;
import org.springframework.http.HttpStatus;

//Bad Gateway에러 시 사용
public class Exception502 extends ServerException{
    public Exception502(BaseExceptionStatus exception) {
        super(exception.getMessage());
    }

    @Override
    public ApiUtils.ApiResult<?> body() {return ApiUtils.error(getMessage(), HttpStatus.BAD_GATEWAY.value());}

    @Override
    public HttpStatus status() { return HttpStatus.BAD_GATEWAY;}
}
