package com.kakao.linknamu.bookmarkTag.repository;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTagId;
import com.kakao.linknamu.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkTagJPARepository extends JpaRepository<BookmarkTag, BookmarkTagId> {
    @Query("select bt.tag.tagName from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
    List<String> findTagNamesByBookmarkId(@Param("bookmarkId") Long bookmarkId);

    @Query("select bt.tag.tagId from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
    List<Long> findTagIdByBookmarkId(@Param("bookmarkId") Long bookmarkId);

    @Query("select bt from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId and bt.tag.tagName = :tagName")
    BookmarkTag findMatchingBookmarkTag(@Param("bookmarkId") Long bookmarkId, @Param("tagName") String tagName);

    @Query("select bt.bookmark from BookmarkTag bt where bt.bookmark.bookmarkName = :bookmarkName and bt.tag.tagName in :tags")
    Optional<List<Bookmark>> findMatchingBookmarks(String bookmarkName, List<String> tags);

    @Query("select bt.tag from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
    List<Tag> findTagByBookmarkId(@Param("bookmarkId") Long bookmarkId);
}
