package com.kakao.linknamu.thirdparty.googledocs.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterGoogleDocsRequestDto(
	@NotBlank
	String documentId
) {
}
