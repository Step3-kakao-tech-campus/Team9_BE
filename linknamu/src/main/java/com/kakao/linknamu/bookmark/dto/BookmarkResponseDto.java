package com.kakao.linknamu.bookmark.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.tag.entity.Tag;

import lombok.Builder;

public class BookmarkResponseDto {

	public record BookmarkUpdateResponseDto(
		Long bookmarkId,
		String title,
		String description,
		String url,
		String imageUrl,
		List<String> tags
	) {
		@Builder
		public BookmarkUpdateResponseDto {
		}
	}

	@SuppressWarnings("checkstyle:RegexpSingleline")
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

		public static BookmarkGetResponseDto of(Bookmark bookmark, List<Tag> tagList) {
			return BookmarkGetResponseDto.builder()
				.bookmarkId(bookmark.getBookmarkId())
				.title(bookmark.getBookmarkName())
				.description(bookmark.getBookmarkDescription())
				.url(bookmark.getBookmarkLink())
				.imageUrl(bookmark.getBookmarkThumbnail())
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
