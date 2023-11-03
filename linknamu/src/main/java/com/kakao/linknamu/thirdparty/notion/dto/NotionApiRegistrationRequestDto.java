package com.kakao.linknamu.thirdparty.notion.dto;

import jakarta.validation.constraints.NotBlank;

public record NotionApiRegistrationRequestDto(
        @NotBlank String code,
        @NotBlank String pageId
) {
}
