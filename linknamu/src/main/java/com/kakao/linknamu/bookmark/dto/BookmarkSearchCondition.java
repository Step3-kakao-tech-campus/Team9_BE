package com.kakao.linknamu.bookmark.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record BookmarkSearchCondition(
	@NotBlank(message = "북마크 이름은 공백이 될 수 없습니다.")
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
