package com.kakao.linknamu.thirdparty.googledocs.controller;

import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.thirdparty.googledocs.dto.RegisterGoogleDocsRequestDto;
import com.kakao.linknamu.thirdparty.googledocs.service.GoogleDocsApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google-docs")
public class GoogleDocsController {

	private final GoogleDocsApiService googleDocsApiService;

	@PostMapping("/registration")
	public ResponseEntity<?> registerDocs(
		@RequestBody @Valid RegisterGoogleDocsRequestDto dto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		googleDocsApiService.createDocsApi(dto, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/delete/{docsPageId}")
	public ResponseEntity<?> deleteDocs(
		@PathVariable(name = "docsPageId") Long docsPageId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		googleDocsApiService.deleteDocsPage(customUserDetails.getUser(), docsPageId);
		return ResponseEntity.ok(ApiUtils.success(null));
	}
}
