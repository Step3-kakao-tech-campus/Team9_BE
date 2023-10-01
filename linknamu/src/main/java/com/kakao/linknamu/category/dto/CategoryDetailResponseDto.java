package com.kakao.linknamu.category.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CategoryDetailResponseDto(
        PageInfoDto pageInfo,
        List<CategoryContentDto> categoryContents,
        List<BookmarkContentDto> bookmarkContents
) {
    private record CategoryContentDto(
            Long categoryId,
            String categoryName
    ) {}

    private record BookmarkContentDto(
            Long bookmarkId,
            String title,
            String info,
            String url,
            String imageUrl,
            List<TagDto> tags,
            LocalDateTime createdAt
    ) {
        private record TagDto(
                Long tagId,
                String tagName
        ) {}
    }
}
