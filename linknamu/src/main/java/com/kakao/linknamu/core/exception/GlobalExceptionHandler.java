package com.kakao.linknamu.core.exception;

import com.kakao.linknamu.core.util.ApiUtils;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j

public class GlobalExceptionHandler {


	@ExceptionHandler({ConstraintViolationException.class})
	public ResponseEntity<?> pathVarValidationException(ConstraintViolationException exception) {
		ApiUtils.ApiResult<?> apiResult = ApiUtils.error(exception.getMessage(),
			HttpStatus.BAD_REQUEST.value(), "04000");
		return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<?> dtoValidationException(MethodArgumentNotValidException exception) {
		List<ObjectError> errors = exception.getBindingResult().getAllErrors();
		return new ResponseEntity<>(ApiUtils.error(errors.get(0).getDefaultMessage(),
			HttpStatus.BAD_REQUEST.value(), "04001"),
			HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> inValidInput() {
		ApiUtils.ApiResult<?> apiResult = ApiUtils.error("정상적인 입력이 아닙니다.", HttpStatus.BAD_REQUEST.value(), "04002");
		return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public ResponseEntity<?> dtoTypeMismatchException() {
		return new ResponseEntity<>(ApiUtils.error("입력의 타입이 올바르지 않습니다.", HttpStatus.BAD_REQUEST.value(), "04003"),
			HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({MissingServletRequestPartException.class})
	public ResponseEntity<?> requestPartMissingException() {
		return new ResponseEntity<>(
			ApiUtils.error("form-data 형식에 file이라는 이름이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value(), "84001"),
			HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({Exception400.class, Exception401.class, Exception403.class, Exception404.class})
	public ResponseEntity<?> clientException(ClientException exception) {
		return new ResponseEntity<>(exception.body(), exception.status());
	}

	@ExceptionHandler({Exception500.class})
	public ResponseEntity<?> serverException(ServerException exception) {
		return new ResponseEntity<>(exception.body(), exception.status());
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<?> unknownServerError(Exception exception) {

		return new ResponseEntity<>(
			ApiUtils.error("서버에서 알 수 없는 에러가 발생했습니다."
					+ exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"05000"),
			HttpStatus.INTERNAL_SERVER_ERROR
		);
	}
}
