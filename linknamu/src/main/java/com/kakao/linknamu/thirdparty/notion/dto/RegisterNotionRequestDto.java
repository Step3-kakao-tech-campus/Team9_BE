package com.kakao.linknamu.thirdparty.notion.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterNotionRequestDto(
	@NotBlank String code,
	@NotBlank String pageId

) {
}
