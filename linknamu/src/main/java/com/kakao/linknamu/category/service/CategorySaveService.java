package com.kakao.linknamu.category.service;

import com.kakao.linknamu._core.exception.Exception400;
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

//    public void save(CategorySaveRequestDto requestDto, User user) {
//        Category parentCategory = categoryService.findById(requestDto.parentCategoryId());
//
//        categoryService.validUser(parentCategory, user);
//        categoryService.findByParentCategoryIdAndCategoryName(requestDto.parentCategoryId(), requestDto.categoryName()).ifPresent((c) -> {
//                throw new Exception400(CategoryExceptionStatus.CATEGORY_ALREADY_EXISTS);
//        });
//
//        categoryService.save(requestDto.categoryName(), parentCategory, user);
//    }

}
