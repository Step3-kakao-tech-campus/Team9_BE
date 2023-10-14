package com.kakao.linknamu.share.dto;

import jakarta.validation.constraints.Positive;

public record CreateWorkSpaceLinkRequestDto(
        @Positive(message = "올바른 워크스페이스 id값을 넣어주세요")
        Long workSpaceId
) {
}
