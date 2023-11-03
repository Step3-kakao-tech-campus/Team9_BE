package com.kakao.linknamu.bookmark.dto;

import java.util.List;

import lombok.Builder;

public record BookmarkSearchCondition(
	String bookmarkName,
	String bookmarkLink,
	String bookmarkDescription,
	List<String> tags,
	String workspaceName
) {

	@Builder
	public BookmarkSearchCondition {
	}
}
