package com.kakao.linknamu.category.repository;

import com.kakao.linknamu.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJPARepository extends JpaRepository<Category, Long> {
}
