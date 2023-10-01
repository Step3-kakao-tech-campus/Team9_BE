package com.kakao.linknamu.bookmark.controller;

import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.dto.BookmarkResponseDto;
import com.kakao.linknamu.bookmark.service.BookmarkCreateService;
import com.kakao.linknamu.bookmark.service.BookmarkDeleteService;
import com.kakao.linknamu.bookmark.service.BookmarkSearchService;
import com.kakao.linknamu.bookmark.service.BookmarkUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {
    private final BookmarkCreateService bookmarkCreateService;
    private final BookmarkSearchService bookmarkSearchService;
    private final BookmarkDeleteService bookmarkDeleteService;
    private final BookmarkUpdateService bookmarkUpdateService;

    @PostMapping("/create")
    public ResponseEntity<?> bookmarkCreate(
            @RequestBody @Valid BookmarkRequestDto.bookmarkAddDTO dto,
            Error error
    ) {
        bookmarkCreateService.bookmarkAdd(dto);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @GetMapping("/search")
    public ResponseEntity<?> bookmarkSearch(
            @RequestParam("search") String search,
            @RequestParam("tag") List<String> tags
    ) {
        List<BookmarkResponseDto.SearchDto> dto = bookmarkSearchService.bookmarkSearch(search, tags);
        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    @PostMapping("/delete/{bookmark_id}")
    public ResponseEntity<?> bookmarkDelete(
            @PathVariable Long bookmark_id
    ) {
        bookmarkDeleteService.bookmarkDelete(bookmark_id);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/update/{bookmark_id}")
    public ResponseEntity<?> bookmarkUpdate(
            @RequestBody @Valid BookmarkRequestDto.bookmarkUpdateRequestDto dto,
            @PathVariable Long bookmark_id
    ) {
        BookmarkResponseDto.bookmarkUpdateResponseDto responseDto = bookmarkUpdateService.bookmarkUpdate(dto, bookmark_id);
        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }
}
