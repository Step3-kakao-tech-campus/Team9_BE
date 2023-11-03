package com.kakao.linknamu.share.dto.category;

import jakarta.validation.constraints.Positive;

public record CreateCategoryFromEncodedIdRequestDto(
	@Positive(message = "카테고리가 저장될 올바른 워크스페이스 id값은 양수여야 합니다.")
	Long workSpaceId
) {
}