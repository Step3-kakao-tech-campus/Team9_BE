package com.kakao.linknamu.thirdparty.notion.controller;

import com.kakao.linknamu.thirdparty.notion.dto.NotionApiRegistrationRequestDto;
import com.kakao.linknamu.thirdparty.notion.service.NotionApiCreateService;
import com.kakao.linknamu.thirdparty.notion.service.NotionApiDeleteService;
import com.kakao.linknamu.thirdparty.notion.util.NotionProvider;
import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.core.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notion")
public class NotionController {

    private final NotionApiCreateService notionApiCreateService;
    private final NotionApiDeleteService notionApiDeleteService;
    private final NotionProvider notionProvider;

    @PostMapping("/registration")
    public ResponseEntity<?> apiRegistration(@RequestBody @Valid NotionApiRegistrationRequestDto requestDto,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        // api 등록 로직
        String accessToken = notionProvider.getAccessToken(requestDto.code());
        notionApiCreateService.createNotionApi(accessToken, requestDto, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

	@PostMapping("/delete/{notion_account}")
	public ResponseEntity<?> apiDelete(@PathVariable(name = "notion_account") Long notionAccountId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		//api 등록 취소 로직
		notionApiDeleteService.deleteNotionAccount(customUserDetails.getUser(), notionAccountId);
		return ResponseEntity.ok(ApiUtils.success(null));
	}
}
