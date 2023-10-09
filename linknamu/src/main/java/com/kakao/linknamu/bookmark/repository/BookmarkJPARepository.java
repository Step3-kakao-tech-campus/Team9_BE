package com.kakao.linknamu.bookmark.repository;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkJPARepository extends JpaRepository<Bookmark, Long> {
    @Modifying
    @Query("update Bookmark b set b.bookmarkName = :bookmarkName, b.bookmarkDescription = :bookmarkDescription where b.bookmarkId = :bookmarkId")
    void updateBookmark(@Param("bookmarkId") Long bookmarkId, @Param("bookmarkName") String bookmarkName, @Param("bookmarkDescription") String bookmarkDescription);

    @Query("select b from Bookmark b " +
            "join fetch b.category c " +
            "join fetch c.workspace w " +
            "where b.bookmarkId = :bookmarkId")
    Optional<Bookmark> findByIdFetchJoinCategoryAndWorkspace(@Param("bookmarkId") Long bookmarkId);

    @Query("select b from Bookmark b where b.category.categoryId = :categoryId")
    Page<Bookmark> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("select b from Bookmark b where b.category.categoryId = :categoryId and b.bookmarkLink = :bookmarkLink")
    Optional<Bookmark> findByCategoryIdAndBookmarkLink(@Param("categoryId") Long categoryId, @Param("bookmarkLink") String bookmarkLink);

    @Query("select b from Bookmark b " +
            "join fetch b.category c " +
            "join fetch c.workspace w " +
            "where b.bookmarkId in :bookmarkIds")
    List<Bookmark> searchRequiredBookmarks(@Param("bookmarkIds") List<Long> bookmarkIds);

    @Query("select b from Bookmark b " +
            "where b.bookmarkName like concat('%',:keyword,'%') and " +
            "b.category.workspace.user.userId = :userId ")
    Page<Bookmark> searchByBookmarkName(@Param("keyword") String keyword, @Param("userId") Long userId, Pageable pageable);

    @Query("select b from Bookmark b " +
            "where b.bookmarkLink like concat('%',:keyword,'%') and " +
            "b.category.workspace.user.userId = :userId")
    Page<Bookmark> searchByBookmarkLink(@Param("keyword") String keyword, @Param("userId") Long userId, Pageable pageable);

    @Query("select b from Bookmark b " +
            "where b.bookmarkDescription like concat('%',:keyword,'%') and " +
            "b.category.workspace.user.userId = :userId")
    Page<Bookmark> searchByBookmarkDescription(@Param("keyword") String keyword, @Param("userId") Long userId, Pageable pageable);
}
