package com.kakao.linknamu.category.dto;

import com.kakao.linknamu.category.entity.Category;
import lombok.Builder;

import java.util.List;

public record ChildCategoryListResponseDto(
        PageInfoDto pageInfo,
        List<ChildCategoryDto> childCategoryList
) {

    @Builder
    public ChildCategoryListResponseDto{
    }

    public static ChildCategoryListResponseDto of(PageInfoDto pageInfoDto, List<Category> categoryList){
        return ChildCategoryListResponseDto.builder()
                .pageInfo(pageInfoDto)
                .childCategoryList(categoryList.stream().map(ChildCategoryDto::of).toList())
                .build();
    }

    private record ChildCategoryDto(
            String categoryName,
            Long categoryId
    ) {

        @Builder
        public ChildCategoryDto{
        }

        public static ChildCategoryDto of(Category category){
            return ChildCategoryDto.builder()
                    .categoryName(category.getCategoryName())
                    .categoryId(category.getCategoryId())
                    .build();
        }
    }
}
