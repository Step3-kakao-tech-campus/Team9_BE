package com.kakao.linknamu.category.service;

import com.kakao.linknamu._core.exception.Exception403;
import com.kakao.linknamu.category.CategoryExceptionStatus;
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
        Category parentCategory = categoryService.findById(requestDto.parentCategoryId());
        if (!parentCategory.getUser().getUserId().equals(user.getUserId())){
            throw new Exception403(CategoryExceptionStatus.CATEGORY_FORBIDDEN);
        }
        categoryService.save(requestDto.categoryName(), parentCategory, user);
    }

}
