package com.kakao.linknamu.bookmark.dto;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.category.entity.Category;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BookmarkRequestDto {
	@Getter
	@Setter
	public static class BookmarkAddDto {

		@NotBlank(message = "북마크 이름은 공백이 될 수 없습니다.")
		@Size(max = 100, message = "북마크 이름은 최대 100자까지 가능합니다.")
		private String bookmarkName;

		@NotBlank(message = "북마크 링크는 공백이 될 수 없습니다.")
		@Pattern(regexp = "https?://[a-zA-Z0-9\\-.]+(:[0-9]+)?\\.[a-zA-Z]{2,3}(\\S*)?", message = "잘못된 웹 링크입니다.")
		@Size(max = 1024, message = "너무 긴 링크입니다.")
		private String bookmarkLink;

		@Size(max = 200, message = "북마크 설명을 최대 200자까지 가능합니다.")
		private String bookmarkDescription;

		@NotNull(message = "카테고리 ID를 입력해주세요.")
		private Long categoryId;

		private String imageUrl;

		@Size(max = 50, message = "태그는 최대 50자까지 가능합니다.")
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

