package com.kakao.linknamu.category.service;

import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryDeleteService {

    private final CategoryService categoryService;

    public void delete(Long categoryId, User user) {
        Category category = categoryService.findByIdFetchJoinWorkspace(categoryId);
        categoryService.validUser(category.getWorkspace(), user);
        categoryService.deleteById(categoryId);
    }
}
