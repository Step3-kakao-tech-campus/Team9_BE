package com.kakao.linknamu.bookmarkTag.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.bookmarkTag.dto.CreateBookmarkTagRequestDto;
import com.kakao.linknamu.bookmarkTag.dto.DeleteBookmarkTagRequestDto;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagDeleteService;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tag")
public class BookmarkTagController {
    private final BookmarkTagSaveService bookmarkTagSaveService;
    private final BookmarkTagDeleteService bookmarkTagDeleteService;

    @PostMapping("/create/{bookmark_id}")
    public ResponseEntity<?> createBookmarkTag(@PathVariable(value = "bookmark_id") Long bookmarkId,
                                               @RequestBody CreateBookmarkTagRequestDto requestDto,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 북마크 태크 생성
        bookmarkTagSaveService.create(requestDto, userDetails.getUser(), bookmarkId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteBookmarkTag(@RequestBody DeleteBookmarkTagRequestDto requestDto,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 북마크 태그 제거
        bookmarkTagDeleteService.deleteByTagIdAndBookmarkId(requestDto, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
