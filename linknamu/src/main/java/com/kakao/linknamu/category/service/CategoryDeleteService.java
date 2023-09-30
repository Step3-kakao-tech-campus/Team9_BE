package com.kakao.linknamu.category.service;

import com.kakao.linknamu.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryDeleteService {

    private final CategoryService categoryService;

    public void delete(Long categoryId) {
        Category category = categoryService.findById(categoryId);
        categoryService.deleteById(categoryId);
    }
}
