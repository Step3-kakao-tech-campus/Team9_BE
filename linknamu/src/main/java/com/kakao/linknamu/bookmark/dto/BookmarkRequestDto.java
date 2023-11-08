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
	public static class BookmarkAddDto {

		@NotEmpty
		private String bookmarkName;

		@NotEmpty
		@Pattern(regexp = "https?://[a-zA-Z0-9\\-.]+(:[0-9]+)?\\.[a-zA-Z]{2,3}(\\S*)?", message = "잘못된 웹 링크입니다.")
		private String bookmarkLink;

		private String bookmarkDescription;

		@NotNull
		private Long categoryId;

		private String imageUrl;

		private List<String> tags;

		public Bookmark toEntity(Category category, String imageUrl) {
			return Bookmark.builder()
				.bookmarkName(bookmarkName)
				.bookmarkLink(bookmarkLink)
				.bookmarkDescription(bookmarkDescription)
				.category(category)
				.bookmarkThumbnail(imageUrl)
				.build();
		}
	}

	public record BookmarkUpdateRequestDto(
		String bookmarkName,
		String description
	) {
		@Builder
		public BookmarkUpdateRequestDto {
		}
	}

	public record BookmarkMoveRequestDto(
		List<Long> bookmarkIdList,
		Long toCategoryId
	) {
		@Builder
		public BookmarkMoveRequestDto {
		}
	}
}

