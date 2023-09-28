package com.kakao.linknamu.category.service;

import com.kakao.linknamu.category.dto.CategoryListResponseDto;
import com.kakao.linknamu.category.dto.PageInfoDto;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CategoryListService {

    private final CategoryService categoryService;

    public CategoryListResponseDto findByUserId(Pageable pageable, User user) {
        Page<Category> categoryPage = categoryService.findByUserId(pageable, user);
        PageInfoDto pageInfoDto = PageInfoDto.of(pageable, categoryPage.getTotalElements(), categoryPage.getTotalPages());
        return CategoryListResponseDto.of(pageInfoDto, categoryPage.getContent());
    }
}
