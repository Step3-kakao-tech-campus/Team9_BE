package com.kakao.linknamu.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkspaceUpdateRequestDto(
	@NotBlank(message = "워크스페이스의 제목은 공백이 될 수 없습니다.")
	@Size(max = 50, message = "워크스페이스의 제목은 50자를 초과할 수 없습니다.")
	String workspaceName
) {
}
