package com.kakao.linknamu.bookmark.dto;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.tag.entity.Tag;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class BookmarkResponseDto {

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

    public record BookmarkGetResponseDto(
            Long bookmarkId,
            String title,
            String description,
            String url,
            String imageUrl,
            List<TagDto> tagList,
            LocalDateTime createdAt
    ) {
        @Builder
        public BookmarkGetResponseDto {
        }

        public static BookmarkGetResponseDto of(BookmarkUserQueryDto bookmark, List<Tag> tagList) {
            return BookmarkGetResponseDto.builder()
                    .bookmarkId(bookmark.getBookmarkId())
                    .title(bookmark.getTitle())
                    .description(bookmark.getDescription())
                    .url(bookmark.getUrl())
                    .imageUrl(bookmark.getImageUrl())
                    .tagList(tagList.stream().map(TagDto::of).toList())
                    .createdAt(bookmark.getCreatedAt())
                    .build();
        }
    }

    private record TagDto(
            Long tagId,
            String tagName
    ) {
        private static TagDto of(Tag tag) {
            return new TagDto(tag.getTagId(), tag.getTagName());
        }
    }
}
