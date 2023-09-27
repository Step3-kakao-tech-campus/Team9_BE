package com.kakao.linknamu.bookmark.controller;

import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.bookmark.dto.BookmarkRequestDTO;
import com.kakao.linknamu.bookmark.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("bookmark/create")
    public ResponseEntity<?> bookmarkCreate(@RequestBody @Valid BookmarkRequestDTO.bookmarkAddDTO dto, Error error){
        bookmarkService.bookmarkAdd(dto);
        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
