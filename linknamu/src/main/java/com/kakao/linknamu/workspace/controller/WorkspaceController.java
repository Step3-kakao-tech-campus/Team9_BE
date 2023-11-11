package com.kakao.linknamu.workspace.controller;

import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.workspace.dto.WorkspaceCreateRequestDto;
import com.kakao.linknamu.workspace.dto.WorkspaceUpdateRequestDto;
import com.kakao.linknamu.workspace.service.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspace")
public class WorkspaceController {
	private final WorkspaceService workspaceService;

	@GetMapping("/list")
	public ResponseEntity<?> getWorkspaceList(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(ApiUtils.success(workspaceService.getWorkspaceList(userDetails.getUser())));
	}

	@PostMapping("/create")
	public ResponseEntity<?> createWorkspace(
		@RequestBody @Valid WorkspaceCreateRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		workspaceService.createWorkspace(requestDto.workspaceName(), userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/update/{workspaceId}")
	public ResponseEntity<?> updateWorkspace(
		@PathVariable("workspaceId") Long workspaceId,
		@RequestBody @Valid WorkspaceUpdateRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		workspaceService.updateWorkspaceName(workspaceId, requestDto, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/delete/{workspaceId}")
	public ResponseEntity<?> deleteWorkspace(
		@PathVariable("workspaceId") Long workspaceId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		workspaceService.deleteWorkspace(workspaceId, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}
}
