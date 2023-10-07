package com.kakao.linknamu.bookmark.dto;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.category.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BookmarkRequestDto {
    @Getter
    @Setter
    public static class bookmarkAddDTO {
        @NotEmpty
        private String bookmarkName;

        @NotEmpty
        @Pattern(regexp = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$", message = "잘못된 웹 링크입니다.")
        private String bookmarkLink;

        private String bookmarkDescription;

        @NotNull
        private Long categoryId;

        private String imageUrl;

        private List<String> tags;

        public Bookmark toBookmarkEntity(Category category) {
            return Bookmark.builder()
                    .bookmarkName(bookmarkName)
                    .bookmarkLink(bookmarkLink)
                    .bookmarkDescription(bookmarkDescription)
                    .category(category)
                    .bookmarkThumbnail(imageUrl)
                    .build();
        }
    }

    public record bookmarkUpdateRequestDto (
            String bookmarkName,
            String description
    ) {
        @Builder
        public bookmarkUpdateRequestDto{}
    }

    public record bookmarkMoveRequestDto (
            List<Long> bookmarkIdList,
            Long toCategoryId
    ) {
        @Builder
        public bookmarkMoveRequestDto{}
    }
}

