package com.kakao.linknamu.thirdparty.notion.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterNotionRequestDto(
	@NotBlank(message = "노션 코드는 공백이 될 수 없습니다.")
	String code,
	@NotBlank(message = "노션 페이지 ID는 공백이 될 수 없습니다.")
	String pageId

) {
}
