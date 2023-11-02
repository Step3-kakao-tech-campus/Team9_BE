package com.kakao.linknamu.bookmark.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.core.dto.PageInfoDto;
import com.kakao.linknamu.tag.entity.Tag;

import lombok.Builder;

public record BookmarkSearchResponseDto(
	PageInfoDto pageInfo,
	List<BookmarkContentDto> bookmarkContents
) {

	@Builder
	public BookmarkSearchResponseDto {
	}

	public static BookmarkSearchResponseDto of(PageInfoDto pageInfo, List<BookmarkContentDto> bookmarkContentDtos) {
		return BookmarkSearchResponseDto.builder()
			.pageInfo(pageInfo)
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
		public BookmarkContentDto {
		}

		public static BookmarkContentDto of(Bookmark bookmark, List<Tag> tags) {
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
			public TagDto {
			}

			public static TagDto of(Tag tag) {
				return TagDto.builder()
					.tagId(tag.getTagId())
					.tagName(tag.getTagName())
					.build();
			}
		}
	}
}
