package com.kakao.linknamu.bookmark.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kakao.linknamu.bookmark.dto.BookmarkUserQueryDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;

public interface BookmarkJpaRepository
	extends JpaRepository<Bookmark, Long>, BookmarkJpaRepositoryCustom, BookmarkCustomRepository {
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("update Bookmark b set b.bookmarkName = :bookmarkName, "
		+ "b.bookmarkDescription = :bookmarkDescription "
		+ "where b.bookmarkId = :bookmarkId")
	void updateBookmark(
		@Param("bookmarkId") Long bookmarkId,
		@Param("bookmarkName") String bookmarkName,
		@Param("bookmarkDescription") String bookmarkDescription
	);

	@Query("select b from Bookmark b "
		+ "join fetch b.category c "
		+ "join fetch c.workspace w "
		+ "where b.bookmarkId = :bookmarkId")
	Optional<Bookmark> findByIdFetchJoinCategoryAndWorkspace(@Param("bookmarkId") Long bookmarkId);

	@Query("select new com.kakao.linknamu.bookmark.dto.BookmarkUserQueryDto(b, b.category.workspace.user.userId) "
		+ "from Bookmark b "
		+ "where b.bookmarkId = :bookmarkId")
	Optional<BookmarkUserQueryDto> findByIdBookmarkUserDto(@Param("bookmarkId") Long bookmarkId);

	@Query("select b from Bookmark b where b.category.categoryId = :categoryId")
	Page<Bookmark> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

	@Query("select b from Bookmark b where b.category.categoryId = :categoryId")
	List<Bookmark> findListByCategoryId(@Param("categoryId") Long categoryId);

	@Query("select b from Bookmark b where b.category.categoryId = :categoryId and b.bookmarkLink = :bookmarkLink")
	Optional<Bookmark> findByCategoryIdAndBookmarkLink(@Param("categoryId") Long categoryId,
		@Param("bookmarkLink") String bookmarkLink);

	@Query("select b from Bookmark b "
		+ "join fetch b.category c "
		+ "join fetch c.workspace w "
		+ "where b.bookmarkId in :bookmarkIds")
	List<Bookmark> searchRequiredBookmarks(@Param("bookmarkIds") List<Long> bookmarkIds);

	@Query("select b from Bookmark b "
		+ "where b.category.workspace.user.userId = :userId "
		+ "order by b.createdAt desc")
	Page<Bookmark> recentBookmarks(Pageable pageable, @Param("userId") Long userId);
}
