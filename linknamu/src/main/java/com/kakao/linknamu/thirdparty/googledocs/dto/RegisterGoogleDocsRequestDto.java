package com.kakao.linknamu.thirdparty.googledocs.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterGoogleDocsRequestDto(
	@NotBlank(message = "구글 문서의 ID는 공백이 될 수 없습니다.")
	String documentId
) {
}
