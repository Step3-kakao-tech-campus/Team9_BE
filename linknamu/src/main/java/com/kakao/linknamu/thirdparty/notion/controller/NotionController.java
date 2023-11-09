package com.kakao.linknamu.thirdparty.notion.controller;

import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.thirdparty.notion.dto.RegisterNotionRequestDto;
import com.kakao.linknamu.thirdparty.notion.service.NotionApiService;
import com.kakao.linknamu.thirdparty.notion.util.NotionProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notion")
public class NotionController {

	private final NotionApiService notionApiService;
	private final NotionProvider notionProvider;

	@PostMapping("/registration")
	public ResponseEntity<?> registerNotion(
		@RequestBody @Valid RegisterNotionRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		// api 등록 로직
		String accessToken = notionProvider.getAccessToken(requestDto.code());
		notionApiService.createNotionApi(accessToken, requestDto, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/delete/{notion_account}")
	public ResponseEntity<?> deleteNotion(
		@PathVariable(name = "notion_account") Long notionAccountId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		//api 등록 취소 로직
		notionApiService.deleteNotionAccount(customUserDetails.getUser(), notionAccountId);
		return ResponseEntity.ok(ApiUtils.success(null));
	}
}
