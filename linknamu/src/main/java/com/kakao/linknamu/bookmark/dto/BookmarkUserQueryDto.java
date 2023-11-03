package com.kakao.linknamu.bookmark.dto;

import java.time.LocalDateTime;

import com.kakao.linknamu.bookmark.entity.Bookmark;

import lombok.Getter;

// 북마크와 유저 Id를 합친 데이터
@Getter
public class BookmarkUserQueryDto {
	private Long bookmarkId;
	private String title;
	private String description;
	private String url;
	private String imageUrl;
	private LocalDateTime createdAt;
	private Long userId;

	public BookmarkUserQueryDto(Bookmark bookmark, Long userId) {
		this.bookmarkId = bookmark.getBookmarkId();
		this.title = bookmark.getBookmarkName();
		this.description = bookmark.getBookmarkDescription();
		this.url = bookmark.getBookmarkLink();
		this.imageUrl = bookmark.getBookmarkThumbnail();
		this.createdAt = bookmark.getCreatedAt();
		this.userId = userId;
	}

}
