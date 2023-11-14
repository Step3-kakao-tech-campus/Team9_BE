package com.kakao.linknamu.thirdparty.notion.dto;

import lombok.Builder;

public record NotionKafkaReqeusetDto(
		String pageId,
		String accessToken,
		Long categoryId,
		Long userId
) {
	@Builder
	public NotionKafkaReqeusetDto {
	}
}
