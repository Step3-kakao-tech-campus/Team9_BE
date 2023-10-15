package com.kakao.linknamu.notion.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.notion.dto.NotionApiRegistrationRequestDto;
import com.kakao.linknamu.notion.service.NotionApiCreateService;
import com.kakao.linknamu.notion.service.NotionApiDeleteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notion")
public class NotionController {

    private final NotionApiCreateService notionApiCreateService;
    private final NotionApiDeleteService notionApiDeleteService;

    @PostMapping("/registration")
    public ResponseEntity<?> apiRegistration(@RequestBody @Valid NotionApiRegistrationRequestDto requestDto,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        // api 등록 로직
        notionApiCreateService.createNotionApi(requestDto, userDetails.getUser());
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
