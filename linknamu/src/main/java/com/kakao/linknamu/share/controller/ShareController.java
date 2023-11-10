package com.kakao.linknamu.share.controller;

import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.share.dto.category.CreateCategoryFromEncodedIdRequestDto;
import com.kakao.linknamu.share.dto.category.GetCategoryFromLinkResponseDto;
import com.kakao.linknamu.share.dto.workspace.GetWorkSpaceFromLinkResponseDto;
import com.kakao.linknamu.share.service.category.ShareCategoryService;
import com.kakao.linknamu.share.service.workspace.ShareWorkspaceService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/share")
@Validated
public class ShareController {
	private static final int PAGE_SIZE = 10;
	private final ShareCategoryService shareCategoryService;
	private final ShareWorkspaceService shareWorkspaceService;

	// 워크스페이스
	@GetMapping("/workspace/{workSpaceId}")
	public ResponseEntity<?> createLinkFromWorkSpaceId(
		@PathVariable("workSpaceId") @Positive(message = "id는 양수여야한다.") Long workSpaceId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		String link = shareWorkspaceService.createLink(workSpaceId, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(link));
	}

	@GetMapping("/workspace/link/{encodedWorkSpaceId}")
	public ResponseEntity<?> getWorkspaceFromEncodedId(
		@PathVariable String encodedWorkSpaceId) {
		GetWorkSpaceFromLinkResponseDto responseDto = shareWorkspaceService.getWorkspace(encodedWorkSpaceId);
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}

	@PostMapping("/workspace/link/{encodedWorkSpaceId}")
	public ResponseEntity<?> createWorkspaceFromEncodedId(
		@PathVariable String encodedWorkSpaceId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		shareWorkspaceService.createWorkSpace(encodedWorkSpaceId, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	// 카테고리
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<?> createLinkFromCategoryId(
		@PathVariable("categoryId") @Positive(message = "id는 양수여야한다.") Long categoryId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		String link = shareCategoryService.createLink(categoryId, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(link));
	}

	@GetMapping("/category/link/{encodedCategoryId}")
	public ResponseEntity<?> getCategoryFromEncodedId(
		@PathVariable String encodedCategoryId, @RequestParam(defaultValue = "0") int page) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
		GetCategoryFromLinkResponseDto responseDto = shareCategoryService.getCategory(encodedCategoryId,
			pageable);
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}

	@PostMapping("/category/link/{encodedCategoryId}")
	public ResponseEntity<?> createCategoryFromEncodedId(
		@PathVariable String encodedCategoryId,
		@RequestBody CreateCategoryFromEncodedIdRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		shareCategoryService.createCategory(encodedCategoryId, requestDto, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

}
