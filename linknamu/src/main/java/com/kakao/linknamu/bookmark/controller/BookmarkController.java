package com.kakao.linknamu.bookmark.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.dto.BookmarkResponseDto;
import com.kakao.linknamu.bookmark.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {
    private final BookmarkCreateService bookmarkCreateService;
    private final BookmarkDeleteService bookmarkDeleteService;
    private final BookmarkUpdateService bookmarkUpdateService;
    private final BookmarkMoveService bookmarkMoveService;

    @PostMapping("/create")
    public ResponseEntity<?> bookmarkCreate(
            @RequestBody @Valid BookmarkRequestDto.bookmarkAddDTO dto,
            Error error,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        bookmarkCreateService.bookmarkAdd(dto, user.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/delete/{bookmark_id}")
    public ResponseEntity<?> bookmarkDelete(
            @PathVariable Long bookmark_id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        bookmarkDeleteService.bookmarkDelete(user.getUser().getUserId(), bookmark_id);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/update/{bookmark_id}")
    public ResponseEntity<?> bookmarkUpdate(
            @RequestBody @Valid BookmarkRequestDto.bookmarkUpdateRequestDto dto,
            @PathVariable Long bookmark_id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        BookmarkResponseDto.bookmarkUpdateResponseDto responseDto = bookmarkUpdateService.bookmarkUpdate(dto, user.getUser().getUserId(), bookmark_id);
        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }

    @PostMapping("/move")
    public ResponseEntity<?> bookmarkMove(
            @RequestBody @Valid BookmarkRequestDto.bookmarkMoveRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        bookmarkMoveService.bookmarkMove(dto, user.getUser().getUserId());
        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
