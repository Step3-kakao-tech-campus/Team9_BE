package com.kakao.linknamu.workspace.dto;

import jakarta.validation.constraints.NotBlank;

public record WorkspaceCreateRequestDto(
        @NotBlank String workspaceName
) {
}
