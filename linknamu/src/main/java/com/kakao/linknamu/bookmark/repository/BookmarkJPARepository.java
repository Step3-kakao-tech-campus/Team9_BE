package com.kakao.linknamu.bookmark.repository;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkJPARepository extends JpaRepository<Bookmark, Long> {
    @Query("update Bookmark b set b.bookmarkName = :bookmarkName, b.bookmarkDescription = :bookmarkDescription where b.bookmarkId = :bookmarkId")
    void updateBookmark(@Param("bookmarkId") Long bookmarkId, @Param("bookmarkName") String bookmarkName, @Param("bookmarkDescription") String bookmarkDescription);
}
