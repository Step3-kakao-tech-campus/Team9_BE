package com.kakao.linknamu.bookmarkTag.repository;

import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkTagJPARepository extends JpaRepository<BookmarkTag, BookmarkTagId> {
    @Query("select bt.tag.tagName from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
    List<String> findTagIdsByBookmarkId(@Param("bookmarkId") Long bookmarkId);
}
