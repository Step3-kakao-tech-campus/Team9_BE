package com.kakao.linknamu.bookmarktag.controller;

import com.kakao.linknamu.bookmarktag.dto.CreateBookmarkTagRequestDto;
import com.kakao.linknamu.bookmarktag.dto.DeleteBookmarkTagRequestDto;
import com.kakao.linknamu.bookmarktag.service.BookmarkTagService;
import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tag")
public class BookmarkTagController {
	private final BookmarkTagService bookmarkTagService;

	@PostMapping("/create/{bookmarkId}")
	public ResponseEntity<?> createBookmarkTag(
		@PathVariable(value = "bookmarkId") Long bookmarkId,
		@RequestBody CreateBookmarkTagRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		// 북마크 태그 생성
		bookmarkTagService.create(requestDto, userDetails.getUser(), bookmarkId);
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteBookmarkTag(
		@RequestBody DeleteBookmarkTagRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		// 북마크 태그 제거
		bookmarkTagService.deleteByTagIdAndBookmarkId(requestDto, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}
}
