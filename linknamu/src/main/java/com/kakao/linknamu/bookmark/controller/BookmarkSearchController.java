package com.kakao.linknamu.bookmark.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchByTagDto;
import com.kakao.linknamu.bookmark.service.BookmarkSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookmark/search")
public class BookmarkSearchController {

    private final BookmarkSearchService bookmarkSearchService;

    @GetMapping("")
    public ResponseEntity<?> bookmarkSearch(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam("tag") List<String> tags,
            @AuthenticationPrincipal CustomUserDetails user
            ) {

        BookmarkSearchByTagDto responseDto = bookmarkSearchService.bookmarkSearchByTag(keyword, tags, user.getUser());
        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }
}
