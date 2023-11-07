package com.kakao.linknamu.thirdparty.notion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NotionTokenDto(
	@JsonProperty("access_token") String accessToken,
	@JsonProperty("token_type") String tokenType,
	@JsonProperty("bot_id") String botId,
	@JsonProperty("workspace_name") String workspaceName,
	@JsonProperty("workspace_icon") String workspaceIcon,
	@JsonProperty("workspace_id") String workspaceId,
	@JsonProperty("owner") Owner owner,
	@JsonProperty("duplicated_template_id") String duplicatedTemplateId
) {
	record Owner(
		String type,
		User user) {
		record User(
			String object,
			String id) {
		}
	}
}
