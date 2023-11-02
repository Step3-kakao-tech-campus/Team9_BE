package com.kakao.linknamu.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.dto.CategoryUpdateRequestDto;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryUpdateService {

	private final CategoryService categoryService;

	@Transactional
	public void update(CategoryUpdateRequestDto requestDto, Long categoryId, User user) {
		Category category = categoryService.findByIdFetchJoinWorkspace(categoryId);
		categoryService.validUser(category.getWorkspace(), user);

		// 변경할 카테고리명이 부모 카테고리에 존재하는 경우 예외처리
		categoryService.findByWorkspaceIdAndCategoryName(category.getWorkspace().getId(), requestDto.categoryName())
			.ifPresent((c) -> {
				throw new Exception400(CategoryExceptionStatus.CATEGORY_ALREADY_EXISTS);
			});

		category.updateCategoryName(requestDto.categoryName());
	}
}
