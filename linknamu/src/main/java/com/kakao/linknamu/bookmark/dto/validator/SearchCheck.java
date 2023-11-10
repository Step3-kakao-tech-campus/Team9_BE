package com.kakao.linknamu.bookmark.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SearchCheckValidator.class)
public @interface SearchCheck {
	String message() default "조건에 맞지 않은 필드값입니다.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
