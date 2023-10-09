package com.kakao.linknamu.bookmark.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchDto;
import com.kakao.linknamu.bookmark.service.BookmarkSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookmark/search")
public class BookmarkSearchController {

    private static final int PAGE_SIZE = 10;
    private final BookmarkSearchService bookmarkSearchService;

    @GetMapping("")
    public ResponseEntity<?> bookmarkSearch(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "type", defaultValue = "T") String type,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "tag", defaultValue = "") List<String> tags,
            @AuthenticationPrincipal CustomUserDetails user) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        BookmarkSearchDto responseDto = bookmarkSearchService.bookmarkSearch(type, keyword, tags, user.getUser(), pageable);
        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }

    @PostMapping("/queryDsl")
    public ResponseEntity<?> bookmarkSearch(
            @RequestBody BookmarkSearchCondition condition,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @AuthenticationPrincipal CustomUserDetails user) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        BookmarkSearchDto responseDto = bookmarkSearchService.bookmarkSearchByQueryDsl(condition, user.getUser(), pageable);
        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }
}
