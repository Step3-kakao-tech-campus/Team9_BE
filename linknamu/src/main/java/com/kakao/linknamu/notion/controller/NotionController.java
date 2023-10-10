package com.kakao.linknamu.notion.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.notion.dto.NotionApiRegistrationRequestDto;
import com.kakao.linknamu.notion.service.NotionApiCreateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notion")
public class NotionController {

    private final NotionApiCreateService notionApiCreateService;

    @PostMapping("/registration")
    public ResponseEntity<?> apiRegistration(@RequestBody @Valid NotionApiRegistrationRequestDto requestDto,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        // api 등록 로직
        notionApiCreateService.createNotionApi(requestDto, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
