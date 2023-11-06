package com.kakao.linknamu.share.controller;

import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.share.dto.category.CreateCategoryFromEncodedIdRequestDto;
import com.kakao.linknamu.share.dto.category.GetCategoryFromLinkResponseDto;
import com.kakao.linknamu.share.dto.workspace.GetWorkSpaceFromLinkResponseDto;
import com.kakao.linknamu.share.service.category.CreateCategoryFromEncodedIdService;
import com.kakao.linknamu.share.service.category.CreateLinkFromCategoryService;
import com.kakao.linknamu.share.service.category.GetCategoryFromEncodedIdService;
import com.kakao.linknamu.share.service.workspace.CreateLinkFromWorkspaceService;
import com.kakao.linknamu.share.service.workspace.CreateWorkspaceFromEncodedIdService;
import com.kakao.linknamu.share.service.workspace.GetWorkspaceFromEncodedIdService;
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
	private final CreateLinkFromWorkspaceService createLinkFromWorkspaceService;
	private final GetWorkspaceFromEncodedIdService getWorkspaceFromEncodedIdService;
	private final CreateWorkspaceFromEncodedIdService createWorkspaceFromEncodedIdService;
	private final CreateLinkFromCategoryService createLinkFromCategoryService;
	private final GetCategoryFromEncodedIdService getCategoryFromEncodedIdService;
	private final CreateCategoryFromEncodedIdService createCategoryFromEncodedIdService;

	// 워크스페이스
	@GetMapping("/workspace/{workSpaceId}")
	public ResponseEntity<?> createLinkFromWorkSpaceId(
		@PathVariable("workSpaceId") @Positive(message = "id는 양수여야함.") Long workSpaceId) {
		String link = createLinkFromWorkspaceService.createLink(workSpaceId);
		return ResponseEntity.ok(ApiUtils.success(link));
	}

	@GetMapping("/workspace/link/{encodedWorkSpaceId}")
	public ResponseEntity<?> getWorkspaceFromEncodedId(
		@PathVariable String encodedWorkSpaceId) {
		GetWorkSpaceFromLinkResponseDto responseDto = getWorkspaceFromEncodedIdService.getWorkspace(encodedWorkSpaceId);
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}

	@PostMapping("/workspace/link/{encodedWorkSpaceId}")
	public ResponseEntity<?> createWorkspaceFromEncodedId(
		@PathVariable String encodedWorkSpaceId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		createWorkspaceFromEncodedIdService.createWorkSpace(encodedWorkSpaceId, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	// 카테고리
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<?> createLinkFromCategoryId(
		@PathVariable("categoryId") @Positive(message = "id는 양수여야함.") Long categoryId) {
		String link = createLinkFromCategoryService.createLink(categoryId);
		return ResponseEntity.ok(ApiUtils.success(link));
	}

	@GetMapping("/category/link/{encodedCategoryId}")
	public ResponseEntity<?> getCategoryFromEncodedId(
		@PathVariable String encodedCategoryId, @RequestParam(defaultValue = "0") int page) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
		GetCategoryFromLinkResponseDto responseDto = getCategoryFromEncodedIdService.getCategory(encodedCategoryId,
			pageable);
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}

	@PostMapping("/category/link/{encodedCategoryId}")
	public ResponseEntity<?> createCategoryFromEncodedId(
		@PathVariable String encodedCategoryId,
		@RequestBody CreateCategoryFromEncodedIdRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		createCategoryFromEncodedIdService.createCategory(encodedCategoryId, requestDto, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

}
