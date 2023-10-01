package com.kakao.linknamu._core.util;

import org.springframework.http.HttpStatus;

public class ApiUtils {
    public static <T> ApiResult<T> success(T response) { return new ApiResult<>(true, response, null);}

    public static ApiResult<?> error(String message, int status){
        return new ApiResult<>(false, null, new ApiError(message, status));
    }

    public record ApiResult<T> (
            boolean success,
            T response,
            ApiError error
    ){ }

    public record ApiError(
            String message,
            int status
    ) {}
}
