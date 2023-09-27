package com.kakao.linknamu.category.service;

import com.kakao.linknamu.category.dto.CategorySaveRequestDto;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategorySaveService {

    private final CategoryService categoryService;

    public void save(CategorySaveRequestDto requestDto, User user) {
        Category parentCategory = null;
        if (requestDto.parentCategoryId() != -1){
            parentCategory = categoryService.findById(requestDto.parentCategoryId());
        }
        categoryService.save(requestDto.categoryName(), parentCategory, user);
    }

}
