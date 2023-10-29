package com.kakao.linknamu.share.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.share.dto.category.GetCategoryFromLinkResponseDto;
import com.kakao.linknamu.share.dto.workspace.CreateWorkSpaceLinkRequestDto;
import com.kakao.linknamu.share.dto.workspace.GetWorkSpaceFromLinkResponseDto;
import com.kakao.linknamu.share.service.category.GetCategoryFromLinkService;
import com.kakao.linknamu.share.service.workspace.CreateWorkspaceFromLinkService;
import com.kakao.linknamu.share.service.workspace.CreateWorkspaceLinkService;
import com.kakao.linknamu.share.service.workspace.GetWorkspaceFromLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/share")
public class ShareController {
    private static final int PAGE_SIZE = 10;
    private final GetWorkspaceFromLinkService getWorkSpaceFromLinkService;
    private final GetCategoryFromLinkService getCategoryFromLinkService;
    private final CreateWorkspaceLinkService createWorkspaceLinkService;
    private final CreateWorkspaceFromLinkService createWorkspaceFromLinkService;

    //우선 어떤 액션을 했을떄 url 생성되서 띄워주는 post요청 -> 만들려면 워크스페이스 id랑 유저 정보 필요함
    @PostMapping("/workspace/link")
    public ResponseEntity<?> createWorkSpaceShareURL(@RequestBody @Valid CreateWorkSpaceLinkRequestDto requestDto, Errors errors,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {

        String link = createWorkspaceLinkService.createLink(requestDto);
        return ResponseEntity.ok(ApiUtils.success(link));


    }


    @GetMapping("/workspace/link/{encodedWorkSpaceId}")
    public ResponseEntity<?> getWorkspaceFromURL(
            @PathVariable String encodedWorkSpaceId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        GetWorkSpaceFromLinkResponseDto responseDto = getWorkSpaceFromLinkService.getWorkspace(encodedWorkSpaceId);
        return ResponseEntity.ok(ApiUtils.success(responseDto));


    }


    @GetMapping("/category/link/{encodedCategoryId}")
    public ResponseEntity<?> CreateWorkspaceFromURL(@RequestParam(defaultValue = "0") int page,
                                                    @PathVariable String encodedCategoryId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        GetCategoryFromLinkResponseDto responseDto = getCategoryFromLinkService.getCategory(encodedCategoryId, pageable);
        return ResponseEntity.ok(ApiUtils.success(responseDto));


    }


    @PostMapping("/workspace/link/{encodedWorkSpaceId}")
    public ResponseEntity<?> CreateWorkspaceFromURL(
            @PathVariable String encodedWorkSpaceId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        createWorkspaceFromLinkService.createWorkSpace(encodedWorkSpaceId, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));


    }
}