package com.kakao.linknamu.googleDocs.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleDocsApiRegistrationRequestDto(
        @NotBlank
        String documentId,
        @NotBlank
        String pageName
) {
}
