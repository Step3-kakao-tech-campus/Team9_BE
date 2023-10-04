package com.kakao.linknamu.category.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.category.dto.CategorySaveRequestDto;
import com.kakao.linknamu.category.dto.CategoryUpdateRequestDto;
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

    @PostMapping("/update/{categoryId}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryUpdateRequestDto requestDto,
            Errors errors,
            @AuthenticationPrincipal CustomUserDetails user){

        categoryUpdateService.update(requestDto, categoryId, user.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetails user){

        categoryDeleteService.delete(categoryId, user.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
