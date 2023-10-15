package com.kakao.linknamu.category.repository;

import com.kakao.linknamu.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryJPARepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.workspace.id = :workspaceId and c.categoryName = :categoryName")
    Optional<Category> findByWorkspaceIdAndCategoryName(@Param("workspaceId") Long workspaceId, @Param("categoryName") String categoryName);


    @Query("select c from Category c " +
            "join fetch c.workspace w " +
            "where c.categoryId = :categoryId")
    Optional<Category> findByIdFetchJoinWorkspace(@Param("categoryId") Long categoryId);
}
