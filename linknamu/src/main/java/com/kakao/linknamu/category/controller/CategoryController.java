package com.kakao.linknamu.category.controller;

import com.kakao.linknamu.category.dto.CategoryGetResponseDto;
import com.kakao.linknamu.category.dto.CategorySaveRequestDto;
import com.kakao.linknamu.category.dto.CategoryUpdateRequestDto;
import com.kakao.linknamu.category.service.*;
import com.kakao.linknamu.core.security.CustomUserDetails;
import com.kakao.linknamu.core.util.ApiUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

	private static final int PAGE_SIZE = 10;
	private final CategoryService categoryService;

	@PostMapping("/create")
	public ResponseEntity<?> createCategory(
		@RequestBody @Valid CategorySaveRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		categoryService.createCategory(requestDto, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<?> getCategory(
		@RequestParam(defaultValue = "0") int page,
		@PathVariable Long categoryId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
		CategoryGetResponseDto responseDto = categoryService.getCategory(categoryId, userDetails.getUser(), pageable);
		return ResponseEntity.ok(ApiUtils.success(responseDto));
	}

	@PostMapping("/update/{categoryId}")
	public ResponseEntity<?> updateCategory(
		@PathVariable Long categoryId,
		@RequestBody @Valid CategoryUpdateRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		categoryService.update(requestDto, categoryId, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}

	@PostMapping("/delete/{categoryId}")
	public ResponseEntity<?> deleteCategory(
		@PathVariable Long categoryId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		categoryService.delete(categoryId, userDetails.getUser());
		return ResponseEntity.ok(ApiUtils.success(null));
	}
}
