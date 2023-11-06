package com.kakao.linknamu.workspace.dto;

import jakarta.validation.constraints.NotBlank;

public record WorkspaceUpdateRequestDto(
	@NotBlank String workspaceName
) {
}
