package com.kakao.linknamu.bookmark.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchResponseDto;
import com.kakao.linknamu.bookmark.service.BookmarkSearchService;
import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookmark/search")
public class BookmarkSearchController {

	private static final int PAGE_SIZE = 10;
	private final BookmarkSearchService bookmarkSearchService;

	@PostMapping("")
	public ResponseEntity<?> bookmarkSearch(
		@RequestBody BookmarkSearchCondition condition,
		@RequestParam(name = "page", defaultValue = "0") int page,
		@AuthenticationPrincipal CustomUserDetails user) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		BookmarkSearchResponseDto responseDto = bookmarkSearchService.bookmarkSearch(condition, user.getUser(),
			pageable);
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}
}
