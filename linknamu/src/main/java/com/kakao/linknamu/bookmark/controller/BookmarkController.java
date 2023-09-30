package com.kakao.linknamu.bookmark.controller;

import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.service.BookmarkCreateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {
    private final BookmarkCreateService bookmarkCreateService;

    @PostMapping("/create")
    public ResponseEntity<?> bookmarkCreate(@RequestBody @Valid BookmarkRequestDto.bookmarkAddDTO dto, Error error){
        bookmarkCreateService.bookmarkAdd(dto);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @GetMapping("/search")
    public ResponseEntity<?> bookmarkSearch(
            @RequestParam("search") String search,
            @RequestParam("tag") String tags) {

        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
