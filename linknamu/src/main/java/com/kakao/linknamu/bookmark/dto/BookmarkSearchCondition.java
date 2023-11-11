package com.kakao.linknamu.bookmark.dto;

import java.util.List;

import com.kakao.linknamu.bookmark.dto.validator.SearchCheck;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

// 북마크 이름과 태그의 입력이 없다면 예외를 발생시킨다.
@SearchCheck(message = "북마크 이름 혹은 태그를 입력해주세요.")
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
