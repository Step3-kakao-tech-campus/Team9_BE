package com.kakao.linknamu.category.service;

import com.kakao.linknamu.category.dto.CategoryUpdateRequestDto;
import com.kakao.linknamu.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryUpdateService {

    private final CategoryService categoryService;

    @Transactional
    public void update(CategoryUpdateRequestDto requestDto, Long categoryId) {
        Category category = categoryService.findById(categoryId);
        category.updateCategoryName(requestDto.categoryName());
    }
}
