package com.kakao.linknamu.core.util;

public class ApiUtils {
    public static <T> ApiResult<T> success(T response) { return new ApiResult<>(true, response, null);}

    public static ApiResult<?> error(String message, int status, String errorCode){
        return new ApiResult<>(false, null, new ApiError(message, status, errorCode));
    }

    public record ApiResult<T> (
            boolean success,
            T response,
            ApiError error
    ){ }

    public record ApiError(
            String message,
            int status,
			String errorCode
    ) {}
}
