package com.kakao.linknamu.bookmark.dto;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.tag.entity.Tag;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record BookmarkSearchByTagDto(
        int count,
        List<BookmarkContentDto> bookmarkContents
) {

    @Builder
    public BookmarkSearchByTagDto{}

    public static BookmarkSearchByTagDto of(List<BookmarkContentDto> bookmarkContentDtos){
        return BookmarkSearchByTagDto.builder()
                .count(bookmarkContentDtos.size())
                .bookmarkContents(bookmarkContentDtos)
                .build();
    }

    public record BookmarkContentDto(
            Long bookmarkId,
            String title,
            String description,
            String url,
            String imageUrl,
            List<TagDto> tags,
            LocalDateTime createdAt
    ) {

        @Builder
        public BookmarkContentDto{}

        public static BookmarkContentDto of(Bookmark bookmark, List<Tag> tags){
            return BookmarkContentDto.builder()
                    .bookmarkId(bookmark.getBookmarkId())
                    .title(bookmark.getBookmarkName())
                    .description(bookmark.getBookmarkDescription())
                    .url(bookmark.getBookmarkLink())
                    .imageUrl(bookmark.getBookmarkThumbnail())
                    .tags(tags.stream().map(TagDto::of).toList())
                    .createdAt(bookmark.getCreatedAt())
                    .build();
        }

        private record TagDto(
                Long tagId,
                String tagName
        ) {

            @Builder
            public TagDto{}

            public static TagDto of(Tag tag){
                return TagDto.builder()
                        .tagId(tag.getTagId())
                        .tagName(tag.getTagName())
                        .build();
            }
        }
    }
}
