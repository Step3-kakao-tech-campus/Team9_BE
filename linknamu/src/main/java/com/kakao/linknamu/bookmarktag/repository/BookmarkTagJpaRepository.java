package com.kakao.linknamu.bookmarktag.repository;

import com.kakao.linknamu.bookmarktag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarktag.entity.BookmarkTagId;
import com.kakao.linknamu.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkTagJpaRepository
	extends JpaRepository<BookmarkTag, BookmarkTagId>, BookmarkTagJpaRepositoryCustom {
	@Query("select bt.tag.tagName from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
	List<String> findTagNamesByBookmarkId(@Param("bookmarkId") Long bookmarkId);

	@Query("select bt.tag.tagId from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
	List<Long> findTagIdsByBookmarkId(@Param("bookmarkId") Long bookmarkId);


	@Query("select bt from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId and bt.tag.tagName = :tagName")
	BookmarkTag findMatchingBookmarkTag(@Param("bookmarkId") Long bookmarkId, @Param("tagName") String tagName);

	@Query("select bt.tag from BookmarkTag bt where bt.bookmark.bookmarkId = :bookmarkId")
	List<Tag> findTagByBookmarkId(@Param("bookmarkId") Long bookmarkId);

	@Query("select bt from BookmarkTag bt join fetch bt.tag t where bt.bookmarkTagId = :bookmarkTagId")
	Optional<BookmarkTag> findByIdFetchJoinTag(@Param("bookmarkTagId") BookmarkTagId bookmarkTagId);

	@Query("select bt from BookmarkTag bt join fetch bt.tag t where bt.bookmark.bookmarkId in :bookmarkIdList")
	List<BookmarkTag> findByBookmarkIdsFetchJoinTag(@Param("bookmarkIdList") List<Long> bookmarkIdList);
}
