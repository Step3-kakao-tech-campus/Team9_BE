package com.kakao.linknamu.thirdparty.googledocs.dto;

import lombok.Builder;

public record GoogleDocsKafkaRequestDto(
	String documentId,
	Long categoryId
) {
	@Builder
	public GoogleDocsKafkaRequestDto {
	}
}
