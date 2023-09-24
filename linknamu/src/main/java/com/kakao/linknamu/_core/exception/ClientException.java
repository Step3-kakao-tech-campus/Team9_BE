package com.kakao.linknamu._core.exception;

import com.kakao.linknamu._core.util.ApiUtils;
import org.springframework.http.HttpStatus;

public abstract class ClientException extends RuntimeException{

    public ClientException(String message) {super(message);}

    abstract ApiUtils.ApiResult<?> body();

    abstract HttpStatus status();
}
