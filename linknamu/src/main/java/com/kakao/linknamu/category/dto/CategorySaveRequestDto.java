package com.kakao.linknamu.category.dto;

import jakarta.validation.constraints.NotNull;

public record CategorySaveRequestDto(
        @NotNull String categoryName,
        @NotNull Long parentCategoryId
) {
}
