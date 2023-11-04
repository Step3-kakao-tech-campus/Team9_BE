package com.kakao.linknamu.category.dto;

import jakarta.validation.constraints.NotNull;

public record CategoryUpdateRequestDto(
	@NotNull String categoryName
) {
}
