package com.kakao.linknamu.thirdparty.googleDocs.controller;

import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.thirdparty.googleDocs.dto.GoogleDocsApiRegistrationRequestDto;
import com.kakao.linknamu.thirdparty.googleDocs.service.GoogleDocsApiCreateService;
import com.kakao.linknamu.thirdparty.googleDocs.service.GoogleDocsApiDeleteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google-docs")
public class GoogleDocsController {

	private final GoogleDocsApiCreateService googleDocsApiCreateService;
	private final GoogleDocsApiDeleteService googleDocsApiDeleteService;

	@PostMapping("/registration")
	public ResponseEntity<?> apiRegistration(@RequestBody @Valid GoogleDocsApiRegistrationRequestDto dto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		googleDocsApiCreateService.createDocsApi(dto, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/delete/{docsPageId}")
	public ResponseEntity<?> apiDelete(@PathVariable(name = "docsPageId") Long docsPageId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		googleDocsApiDeleteService.deleteDocsPage(customUserDetails.getUser(), docsPageId);
		return ResponseEntity.ok(ApiUtils.success(null));
	}
}
