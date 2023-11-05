package com.kakao.linknamu.bookmarktag.dto;

public record DeleteBookmarkTagRequestDto(
	Long bookmarkId,
	Long tagId
) {
}
