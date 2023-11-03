package com.kakao.linknamu.bookmark.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.dto.BookmarkResponseDto;
import com.kakao.linknamu.bookmark.service.BookmarkCreateService;
import com.kakao.linknamu.bookmark.service.BookmarkDeleteService;
import com.kakao.linknamu.bookmark.service.BookmarkMoveService;
import com.kakao.linknamu.bookmark.service.BookmarkReadService;
import com.kakao.linknamu.bookmark.service.BookmarkUpdateService;
import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {
	private final BookmarkCreateService bookmarkCreateService;
	private final BookmarkDeleteService bookmarkDeleteService;
	private final BookmarkUpdateService bookmarkUpdateService;
	private final BookmarkMoveService bookmarkMoveService;
	private final BookmarkReadService bookmarkReadService;

	@PostMapping("/create")
	public ResponseEntity<?> createBookmark(
		@RequestBody @Valid
		BookmarkRequestDto.BookmarkAddDto dto,
		Errors errors,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		bookmarkCreateService.addBookmark(dto, user.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/delete/{bookmarkId}")
	public ResponseEntity<?> deleteBookmark(
		@PathVariable Long bookmarkId,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		bookmarkDeleteService.deleteBookmark(user.getUser().getUserId(), bookmarkId);
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/update/{bookmarkId}")
	public ResponseEntity<?> updateBookmark(
		@RequestBody @Valid BookmarkRequestDto.BookmarkUpdateRequestDto dto,
		@PathVariable Long bookmarkId,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		BookmarkResponseDto.BookmarkUpdateResponseDto responseDto = bookmarkUpdateService.updateBookmark(dto,
			user.getUser().getUserId(), bookmarkId);
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}

	@PostMapping("/move")
	public ResponseEntity<?> moveBookmark(
		@RequestBody @Valid BookmarkRequestDto.BookmarkMoveRequestDto dto,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		bookmarkMoveService.moveBookmark(dto, user.getUser().getUserId());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@GetMapping("{bookmarkId}")
	public ResponseEntity<?> getBookmark(
		@PathVariable("bookmarkId") Long bookmarkId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		BookmarkResponseDto.BookmarkGetResponseDto responseDto = bookmarkReadService.getBookmarkById(bookmarkId,
			userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}
}
