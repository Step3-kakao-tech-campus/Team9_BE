package com.kakao.linknamu.bookmark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJpaRepository;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class BookmarkDeleteService {
	private final BookmarkJpaRepository bookmarkJpaRepository;

	/* bookmarkDelete 동작 */
	// 1. id에 해당하는 북마크 탐색
	// 2. 북마크 삭제
	@Transactional
	public void deleteBookmark(Long userId, Long bookmarkId) {

		Bookmark bookmark = bookmarkJpaRepository.findByIdFetchJoinCategoryAndWorkspace(bookmarkId)
			.orElseThrow(() -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND));

		if (!bookmark.getCategory().getWorkspace().getUser().getUserId().equals(userId)) {
			throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
		}

		bookmarkJpaRepository.delete(bookmark);
	}
}
