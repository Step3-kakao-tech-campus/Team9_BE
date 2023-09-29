package com.kakao.linknamu.category.repository;

import com.kakao.linknamu.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryJPARepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.user.userId = :userId")
    Page<Category> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select c from Category c where c.parentCategory.categoryId = :parentCategoryId")
    Page<Category> findByParentCategoryId(@Param("parentCategoryId") Long parentCategoryId, Pageable pageable);
}
