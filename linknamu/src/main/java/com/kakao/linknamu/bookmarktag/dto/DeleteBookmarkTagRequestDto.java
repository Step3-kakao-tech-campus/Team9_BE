package com.kakao.linknamu.bookmarktag.dto;

import jakarta.validation.constraints.NotNull;

public record DeleteBookmarkTagRequestDto(
	@NotNull(message = "북마크 ID를 입력해주세요.")
	Long bookmarkId,
	@NotNull(message = "태그 ID를 입력해주세요.")
	Long tagId
) {
}
