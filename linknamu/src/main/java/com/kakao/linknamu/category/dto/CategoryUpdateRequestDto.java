package com.kakao.linknamu.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryUpdateRequestDto(
	@NotBlank(message = "카테고리 이름은 공백이 될 수 없습니다.")
	@Size(max = 100) String categoryName
) {
}
