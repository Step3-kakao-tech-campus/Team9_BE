package com.kakao.linknamu.bookmark.dto.validator;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SearchCheckValidator implements ConstraintValidator<SearchCheck, BookmarkSearchCondition> {

	@Override
	public boolean isValid(BookmarkSearchCondition dto, ConstraintValidatorContext context) {
		if (dto.tags() == null) {
			return !(dto.bookmarkName()==null || dto.bookmarkName().isBlank());
		}

		return !(dto.tags().isEmpty() && (dto.bookmarkName()==null || dto.bookmarkName().isBlank()));
	}
}
