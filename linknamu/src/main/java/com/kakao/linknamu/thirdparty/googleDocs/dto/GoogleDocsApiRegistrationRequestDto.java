package com.kakao.linknamu.thirdparty.googleDocs.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleDocsApiRegistrationRequestDto(
        @NotBlank
        String documentId
) {
}
