package com.kakao.linknamu.notion.dto;

import jakarta.validation.constraints.NotBlank;

public record NotionApiRegistrationRequestDto(
        @NotBlank String accessToken,
        @NotBlank String pageId
) {
}
