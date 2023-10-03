package com.kakao.linknamu.category.service;

import com.kakao.linknamu._core.exception.Exception403;
import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJPARepository;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryJPARepository categoryJPARepository;

//    public Category save(String categoryName, Category parentCategory, User user){
//        Category category = Category.builder()
//                .categoryName(categoryName)
//                .parentCategory(parentCategory)
//                .user(user)
//                .build();
//        return categoryJPARepository.save(category);
//    }

    public Category findById(Long id) {
        return categoryJPARepository.findById(id).orElseThrow(
                () -> new Exception404(CategoryExceptionStatus.CATEGORY_NOT_FOUND)
        );
    }

    public Page<Category> findByUserId(Pageable pageable, User user){
        return categoryJPARepository.findByUserId(user.getUserId(), pageable);
    }

//    public Page<Category> findByParentCategoryId(Pageable pageable, Category parentCategory){
//        return categoryJPARepository.findByParentCategoryId(parentCategory.getCategoryId(), pageable);
//    }
//
//    public Optional<Category> findByParentCategoryIdAndCategoryName(Long parentCategoryId, String categoryName){
//        return categoryJPARepository.findByParentCategoryIdAndCategoryName(parentCategoryId, categoryName);
//    }

    public void deleteById(Long categoryId){
        categoryJPARepository.deleteById(categoryId);
    }

    // 카테고리의 유저와 로그인 유저가 같은지 체크
    public void validUser(Category category, User user){
        if (!category.getUser().getUserId().equals(user.getUserId())){
            throw new Exception403(CategoryExceptionStatus.CATEGORY_FORBIDDEN);
        }
    }

}
