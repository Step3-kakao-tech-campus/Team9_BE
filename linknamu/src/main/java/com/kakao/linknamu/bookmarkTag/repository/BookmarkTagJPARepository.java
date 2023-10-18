package com.kakao.linknamu.bookmarkTag.repository;

import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTagId;
import com.kakao.linknamu.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// BookmarkTagJPARepositoryCustom을 상속받아 BookmarkTagJPARepositoryImpl에서 구현된 search 메서드를 사용할 수 있다.
public interface BookmarkTagJPARepository extends JpaRepository<BookmarkTag, BookmarkTagId>, BookmarkTagJPARepositoryCustom {
    @Query("select bt.tag.tagName from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
    List<String> findTagNamesByBookmarkId(@Param("bookmarkId") Long bookmarkId);

    @Query("select bt.tag.tagId from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
    List<Long> findTagIdsByBookmarkId(@Param("bookmarkId") Long bookmarkId);

    @Query("select bt from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId and bt.tag.tagName = :tagName")
    BookmarkTag findMatchingBookmarkTag(@Param("bookmarkId") Long bookmarkId, @Param("tagName") String tagName);

    @Query("select bt.tag from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
    List<Tag> findTagByBookmarkId(@Param("bookmarkId") Long bookmarkId);
}
