package com.kakao.linknamu.category.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.category.dto.CategoryListResponseDto;
import com.kakao.linknamu.category.dto.CategorySaveRequestDto;
import com.kakao.linknamu.category.dto.CategoryUpdateRequestDto;
import com.kakao.linknamu.category.dto.ChildCategoryListResponseDto;
import com.kakao.linknamu.category.service.CategoryDeleteService;
import com.kakao.linknamu.category.service.CategoryReadService;
import com.kakao.linknamu.category.service.CategorySaveService;
import com.kakao.linknamu.category.service.CategoryUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final CategoryReadService categoryReadService;
    private final CategoryUpdateService categoryUpdateService;
    private final CategoryDeleteService categoryDeleteService;

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

        Pageable pageable= PageRequest.of(page, PAGE_SIZE);
        CategoryListResponseDto responseDto = categoryReadService.findByUserId(pageable, user.getUser());
        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getChildCategoryList(
            @RequestParam(defaultValue = "0") int page,
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetails user){

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        ChildCategoryListResponseDto responseDto = categoryReadService.findByParentCategoryId(pageable, user.getUser(), categoryId);
        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }

    @PostMapping("/update/{categoryId}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryUpdateRequestDto requestDto,
            Errors errors,
            @AuthenticationPrincipal CustomUserDetails user){

        categoryUpdateService.update(requestDto, categoryId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetails user){

        categoryDeleteService.delete(categoryId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // 상세 조회 부분은 주석처리 해두겠습니다
//    @GetMapping(value = {"/detail/{categoryId}", "/detail"})
//    public ResponseEntity<?> getCategoryDetail(
//            @RequestParam(defaultValue = "0") int page,
//            @PathVariable(required = false) Long categoryId,
//            @AuthenticationPrincipal CustomUserDetails user){
//
//        CategoryDetailResponseDto responseDto = null;
//        if (categoryId == null){
//            // 메인 페이지 조회 구현
//            return ResponseEntity.ok(ApiUtils.success(responseDto));
//        }
//        // 카테고리 별 상세 조회 구현
//        return ResponseEntity.ok(ApiUtils.success(responseDto));
//    }
}
