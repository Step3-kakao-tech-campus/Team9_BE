package com.kakao.linknamu.bookmark.controller;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchResponseDto;
import com.kakao.linknamu.bookmark.service.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.dto.BookmarkResponseDto;
import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {
	private final BookmarkService bookmarkService;
	private static final int PAGE_SIZE = 10;

	@PostMapping("/create")
	public ResponseEntity<?> createBookmark(
		@RequestBody @Valid
		BookmarkRequestDto.BookmarkAddDto dto,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		bookmarkService.addBookmark(dto, user.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/delete/{bookmarkId}")
	public ResponseEntity<?> deleteBookmark(
		@PathVariable Long bookmarkId,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		bookmarkService.deleteBookmark(bookmarkId, user.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/update/{bookmarkId}")
	public ResponseEntity<?> updateBookmark(
		@RequestBody @Valid BookmarkRequestDto.BookmarkUpdateRequestDto dto,
		@PathVariable Long bookmarkId,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		BookmarkResponseDto.BookmarkUpdateResponseDto responseDto = bookmarkService.updateBookmark(dto, bookmarkId, user.getUser());
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}

	@PostMapping("/move")
	public ResponseEntity<?> moveBookmark(
		@RequestBody @Valid BookmarkRequestDto.BookmarkMoveRequestDto dto,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		bookmarkService.moveBookmark(dto, user.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@GetMapping("{bookmarkId}")
	public ResponseEntity<?> getBookmark(
		@PathVariable("bookmarkId") Long bookmarkId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		BookmarkResponseDto.BookmarkGetResponseDto responseDto = bookmarkService.getBookmarkById(bookmarkId,
			userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}

	@PostMapping("/search")
	public ResponseEntity<?> searchBookmark(
		@RequestBody BookmarkSearchCondition condition,
		@RequestParam(name = "page", defaultValue = "0") int page,
		@AuthenticationPrincipal CustomUserDetails user) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		BookmarkSearchResponseDto responseDto = bookmarkService.searchBookmark(condition, user.getUser(),
			pageable);
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}
}
