package com.kakao.linknamu.bookmarkTag.repository;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTagId;
import com.kakao.linknamu.tag.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkTagJPARepository extends JpaRepository<BookmarkTag, BookmarkTagId> {
    @Query("select bt.tag.tagName from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
    List<String> findTagNamesByBookmarkId(@Param("bookmarkId") Long bookmarkId);

    @Query("select bt.tag.tagId from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
    List<Long> findTagIdsByBookmarkId(@Param("bookmarkId") Long bookmarkId);

    @Query("select bt from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId and bt.tag.tagName = :tagName")
    BookmarkTag findMatchingBookmarkTag(@Param("bookmarkId") Long bookmarkId, @Param("tagName") String tagName);

    @Query("select bt.tag from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
    List<Tag> findTagByBookmarkId(@Param("bookmarkId") Long bookmarkId);

    @Query("select bt.bookmark from BookmarkTag bt " +
            "where bt.bookmark.bookmarkName like concat('%',:keyword,'%') and " +
            "bt.tag.tagName in :tags and " +
            "bt.bookmark.category.workspace.user.userId = :userId " +
            "group by bt.bookmark.bookmarkId having count(bt.tag) = :count")
    Page<Bookmark> findBookmarksByBookmarkNameAndTags(@Param("keyword") String keyword, @Param("tags") List<String> tags, @Param("userId") Long userId, @Param("count") int count, Pageable pageable);

    @Query("select bt.bookmark from BookmarkTag bt " +
            "where bt.bookmark.bookmarkLink like concat('%',:keyword,'%') and " +
            "bt.tag.tagName in :tags and " +
            "bt.bookmark.category.workspace.user.userId = :userId " +
            "group by bt.bookmark.bookmarkId having count(bt.tag) = :count")
    Page<Bookmark> findBookmarksByBookmarkLinkAndTags(@Param("keyword") String keyword, @Param("tags") List<String> tags, @Param("userId") Long userId, @Param("count") int count, Pageable pageable);

    @Query("select bt.bookmark from BookmarkTag bt " +
            "where bt.bookmark.bookmarkDescription like concat('%',:keyword,'%') and " +
            "bt.tag.tagName in :tags and " +
            "bt.bookmark.category.workspace.user.userId = :userId " +
            "group by bt.bookmark.bookmarkId having count(bt.tag) = :count")
    Page<Bookmark> findBookmarksByBookmarkDescriptionAndTags(@Param("keyword") String keyword, @Param("tags") List<String> tags, @Param("userId") Long userId, @Param("count") int count, Pageable pageable);
}
