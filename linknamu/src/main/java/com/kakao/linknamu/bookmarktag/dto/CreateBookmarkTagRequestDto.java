package com.kakao.linknamu.bookmarktag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBookmarkTagRequestDto(
	@NotBlank(message = "태그 이름은 공백이 될 수 없습니다.")
	@Size(max = 50, message = "태그는 최대 50자까지 가능합니다.")
	String tagName
) {
}
