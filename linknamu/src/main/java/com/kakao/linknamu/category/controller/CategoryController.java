package com.kakao.linknamu.category.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.category.dto.CategoryDetailResponseDto;
import com.kakao.linknamu.category.dto.CategoryListResponseDto;
import com.kakao.linknamu.category.dto.CategorySaveRequestDto;
import com.kakao.linknamu.category.service.CategorySaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private static final int PAGE_SIZE = 10;
    private final CategorySaveService categorySaveService;

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(
            @RequestBody @Valid CategorySaveRequestDto requestDto,
            Errors errors,
            @AuthenticationPrincipal CustomUserDetails user){

        categorySaveService.save(requestDto, user.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @GetMapping("")
    public ResponseEntity<?> getCategoryList(
            @RequestParam(defaultValue = "0") int page,
            @AuthenticationPrincipal CustomUserDetails user){
        CategoryListResponseDto responseDto = null;
        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }

    @GetMapping(value = {"/detail/{categoryId}", "/detail"})
    public ResponseEntity<?> getCategoryDetail(
            @RequestParam(defaultValue = "0") int page,
            @PathVariable(required = false) Long categoryId,
            @AuthenticationPrincipal CustomUserDetails user){

        CategoryDetailResponseDto responseDto = null;
        System.out.println(categoryId);
        if (categoryId == null){
            // 메인 페이지 조회 구현
            return ResponseEntity.ok(ApiUtils.success(responseDto));
        }
        // 카테고리 별 상세 조회 구현
        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }
}
