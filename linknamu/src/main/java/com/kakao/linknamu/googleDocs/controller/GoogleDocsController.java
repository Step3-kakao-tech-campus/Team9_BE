package com.kakao.linknamu.googleDocs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.googleDocs.dto.GoogleDocsApiRegistrationRequestDto;
import com.kakao.linknamu.googleDocs.service.GoogleDocsApiCreateService;
import com.kakao.linknamu.googleDocs.service.GoogleDocsApiDeleteService;

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
