package com.kakao.linknamu.category.repository;

import com.kakao.linknamu.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryJPARepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.user.userId = :userId")
    Page<Category> findByUserId(Long userId, Pageable pageable);
}
