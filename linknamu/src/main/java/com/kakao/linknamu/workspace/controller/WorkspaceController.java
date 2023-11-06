package com.kakao.linknamu.workspace.controller;

import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.workspace.dto.WorkspaceCreateRequestDto;
import com.kakao.linknamu.workspace.dto.WorkspaceUpdateRequestDto;
import com.kakao.linknamu.workspace.service.WorkspaceDeleteService;
import com.kakao.linknamu.workspace.service.WorkspaceReadService;
import com.kakao.linknamu.workspace.service.WorkspaceSaveService;
import com.kakao.linknamu.workspace.service.WorkspaceUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspace")
public class WorkspaceController {
	private final WorkspaceReadService workspaceReadService;
	private final WorkspaceSaveService workspaceSaveService;
	private final WorkspaceUpdateService workspaceUpdateService;
	private final WorkspaceDeleteService workspaceDeleteService;

	@GetMapping("/list")
	public ResponseEntity<?> getWorkspaceList(@AuthenticationPrincipal CustomUserDetails userDetails) {
		// 워크스페이스 리스트 조회 서비스 코드
		return ResponseEntity.ok(ApiUtils.success(workspaceReadService.getWorkspaceList(userDetails.getUser())));
	}

	@PostMapping("/create")
	public ResponseEntity<?> createWorkspace(@RequestBody @Valid WorkspaceCreateRequestDto requestDto,
											 @AuthenticationPrincipal CustomUserDetails userDetails) {
		// 워크스페이스 생성 서비스 코드
		workspaceSaveService.createWorkspace(requestDto.workspaceName(), userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	
	@PostMapping("/update/{workspace_id}")
	public ResponseEntity<?> updateWorkspace(@PathVariable("workspace_id") Long workspaceId,
											 @RequestBody @Valid WorkspaceUpdateRequestDto requestDto,
											 @AuthenticationPrincipal CustomUserDetails userDetails) {
		// 워크스페이스 수정 서비스 코드
		workspaceUpdateService.updateWorkspace(workspaceId, requestDto, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/delete/{workspace_id}")
	public ResponseEntity<?> deleteWorkspace(@PathVariable("workspace_id") Long workspaceId,
											 @AuthenticationPrincipal CustomUserDetails userDetails) {
		// 워크스페이스 삭제 서비스 코드
		workspaceDeleteService.deleteWorkspace(workspaceId, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}
}
