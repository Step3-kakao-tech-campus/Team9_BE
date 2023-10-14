package com.kakao.linknamu.share.dto;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.tag.entity.Tag;

import java.util.List;
import java.util.stream.Collectors;

public record GetCategoryFromLinkResponseDto(
        PageInfoDto pageInfo,
        String categoryName,
        List<BookmarkContentDto> bookmarkContents

) {
    public GetCategoryFromLinkResponseDto(PageInfoDto pageInfoDto, Category category, List<Bookmark> bookmarkList,
                                          List<List<Tag>> tagListList) {

        this(pageInfoDto, category.getCategoryName(), bookmarkList.stream()
                .map(bookmark -> new BookmarkContentDto(bookmark, tagListList.get(bookmarkList.indexOf(bookmark))))
                .collect(Collectors.toList()));

    }

    public record BookmarkContentDto(
            Long bookmarkId,
            String title,
            String description,
            String url,
            String imageUrl,
            List<TagDto> tags
    ) {

        public BookmarkContentDto(Bookmark bookmark, List<Tag> tagList) {
            this(
                    bookmark.getBookmarkId(),
                    bookmark.getBookmarkName(),
                    bookmark.getBookmarkDescription(),
                    bookmark.getBookmarkLink(),
                    bookmark.getBookmarkThumbnail(),
                    tagList.stream()
                            .map(tag -> new BookmarkContentDto.TagDto(tag.getTagId(), tag.getTagName()))
                            .collect(Collectors.toList()));
        }

        private record TagDto(
                Long tagId,
                String tagName
        ) {
        }
    }
}



