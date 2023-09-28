package com.kakao.linknamu.category.service;

import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJPARepository;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryJPARepository categoryJPARepository;

    public Category save(String categoryName, Category parentCategory, User user){
        Category category = Category.builder()
                .categoryName(categoryName)
                .parentCategory(parentCategory)
                .user(user)
                .build();
        return categoryJPARepository.save(category);
    }

    public Category findById(Long id) {
        return categoryJPARepository.findById(id).orElseThrow(
                () -> new Exception404(CategoryExceptionStatus.CATEGORY_NOT_FOUND)
        );
    }

    public Page<Category> findByUserId(Pageable pageable, User user){
        return categoryJPARepository.findByUserId(user.getUserId(), pageable);
    }

    public Page<Category> findByUserIdAndParentCategoryId(Pageable pageable, User user, Long parentCategoryId){
        return categoryJPARepository.findByUserIdAndParentCategoryId(user.getUserId(), parentCategoryId, pageable);
    }

}
