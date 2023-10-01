package com.kakao.linknamu.category.dto;

import com.kakao.linknamu.category.entity.Category;
import lombok.Builder;

import java.util.List;

public record CategoryListResponseDto(
        PageInfoDto pageInfo,
        List<CategoryDto> categoryList
) {

    @Builder
    public CategoryListResponseDto{
    }

    public static CategoryListResponseDto of(PageInfoDto pageInfoDto, List<Category> categoryList){
        return CategoryListResponseDto.builder()
                .pageInfo(pageInfoDto)
                .categoryList(categoryList.stream().map(CategoryDto::of).toList())
                .build();
    }

    private record CategoryDto(
            String categoryName,
            Long categoryId
    ) {

        @Builder
        public CategoryDto{
        }

        public static CategoryDto of(Category category){
            return CategoryDto.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .build();
        }
    }
}
