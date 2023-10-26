package com.kakao.linknamu.bookmarkTag.dto;

public record DeleteBookmarkTagRequestDto(
        Long bookmarkId,
        Long tagId
) {
}
