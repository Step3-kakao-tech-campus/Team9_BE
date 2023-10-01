package com.kakao.linknamu.bookmark.dto;

import lombok.Builder;

import java.util.List;

public class BookmarkResponseDto {
    public record SearchDto (
            Long bookmarkId,
            String title,
            String description,
            String url,
            String imageUrl,
            List<String> tags
    ) {
        @Builder
        public SearchDto{}
    }

    public record bookmarkUpdateResponseDto (
            Long bookmarkId,
            String title,
            String description,
            String url,
            String imageUrl,
            List<String> tags
    ) {
        @Builder
        public bookmarkUpdateResponseDto{}
    }
}
