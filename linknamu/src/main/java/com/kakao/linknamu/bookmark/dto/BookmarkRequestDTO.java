package com.kakao.linknamu.bookmark.dto;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.category.Category;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BookmarkRequestDTO {
    @Getter
    @Setter
    public static class bookmarkAddDTO {
        @NotEmpty
        private String bookmarkName;

        @NotEmpty
        private String bookmarkLink;

        private String bookmarkDescription;

        @NotEmpty
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
}

