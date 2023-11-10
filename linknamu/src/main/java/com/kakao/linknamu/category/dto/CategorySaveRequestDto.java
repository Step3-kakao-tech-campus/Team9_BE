package com.kakao.linknamu.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategorySaveRequestDto(
	@NotBlank(message = "카테고리 이름은 공백이 될 수 없습니다.")
	@Size(max = 100, message = "카테고리 이름은 최대 100자까지 가능합니다.")
	String categoryName,
	@NotNull(message = "워크스페이스 ID를 입력해주세요.")
	Long workspaceId
) {
}
