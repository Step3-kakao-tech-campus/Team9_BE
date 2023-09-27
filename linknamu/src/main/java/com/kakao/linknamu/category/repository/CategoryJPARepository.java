package com.kakao.linknamu.category.repository;

import com.kakao.linknamu.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryJPARepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.parentCategory.categoryId = :parentCategoryId")
    Optional<Category> findByParentCategoryId(Long parentCategoryId);
}
