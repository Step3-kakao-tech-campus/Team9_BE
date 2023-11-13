package com.kakao.linknamu.bookmark.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.core.dto.PageInfoDto;
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

	public record BookmarkGetListResponseDto(
		PageInfoDto pageInfo,
		List<BookmarkGetResponseDto> bookmarkContents
	) {
		@Builder
		public BookmarkGetListResponseDto {
		}

		public static BookmarkGetListResponseDto of(
			Page<Bookmark> bookmarkPage,
			Map<Bookmark, List<Tag>> bookmarkListMap) {
			return BookmarkGetListResponseDto.builder()
				.bookmarkContents(
					bookmarkPage.stream()
					.map(b -> BookmarkGetResponseDto.of(b, bookmarkListMap.get(b))).toList()
				)
				.pageInfo(new PageInfoDto(bookmarkPage))
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
