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
	public ResponseEntity<?> bookmarkCreate(
		@RequestBody @Valid BookmarkRequestDto.BookmarkAddDTO dto,
		Errors errors,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		bookmarkCreateService.bookmarkAdd(dto, user.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/delete/{bookmark_id}")
	public ResponseEntity<?> bookmarkDelete(
		@PathVariable Long bookmark_id,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		bookmarkDeleteService.bookmarkDelete(user.getUser().getUserId(), bookmark_id);
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/update/{bookmark_id}")
	public ResponseEntity<?> bookmarkUpdate(
		@RequestBody @Valid BookmarkRequestDto.bookmarkUpdateRequestDto dto,
		@PathVariable Long bookmark_id,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		BookmarkResponseDto.bookmarkUpdateResponseDto responseDto = bookmarkUpdateService.bookmarkUpdate(dto,
			user.getUser().getUserId(), bookmark_id);
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}

	@PostMapping("/move")
	public ResponseEntity<?> bookmarkMove(
		@RequestBody @Valid BookmarkRequestDto.bookmarkMoveRequestDto dto,
		@AuthenticationPrincipal CustomUserDetails user
	) {
		bookmarkMoveService.bookmarkMove(dto, user.getUser().getUserId());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@GetMapping("{bookmark_id}")
	public ResponseEntity<?> getBookmark(
		@PathVariable("bookmark_id") Long bookmarkId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {

		BookmarkResponseDto.BookmarkGetResponseDto responseDto = bookmarkReadService.getBookmarkById(bookmarkId,
			userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}
}
