package com.kakao.linknamu.category.dto;

import java.util.List;

public record CategoryListResponseDto(
        PageInfoDto pageInfo,
        List<CategoryDto> categoryList
) {
    private record CategoryDto(
            String categoryName,
            Long categoryId
    ){}
}
