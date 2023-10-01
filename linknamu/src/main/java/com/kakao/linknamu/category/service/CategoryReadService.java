package com.kakao.linknamu.category.service;

import com.kakao.linknamu.category.dto.CategoryListResponseDto;
import com.kakao.linknamu.category.dto.ChildCategoryListResponseDto;
import com.kakao.linknamu.category.dto.PageInfoDto;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CategoryReadService {

    private final CategoryService categoryService;

    public CategoryListResponseDto findByUserId(Pageable pageable, User user) {
        Page<Category> categoryPage = categoryService.findByUserId(pageable, user);
        PageInfoDto pageInfoDto = PageInfoDto.of(categoryPage);
        return CategoryListResponseDto.of(pageInfoDto, categoryPage.getContent());
    }

    public ChildCategoryListResponseDto findByParentCategoryId(Pageable pageable, Long parentCategoryId, User user){
        Category parentCategory = categoryService.findById(parentCategoryId);
        categoryService.validUser(parentCategory, user);
        Page<Category> childCategoryPage = categoryService.findByParentCategoryId(pageable, parentCategory);
        PageInfoDto pageInfoDto = PageInfoDto.of(childCategoryPage);
        return ChildCategoryListResponseDto.of(pageInfoDto, childCategoryPage.getContent());
    }
}
