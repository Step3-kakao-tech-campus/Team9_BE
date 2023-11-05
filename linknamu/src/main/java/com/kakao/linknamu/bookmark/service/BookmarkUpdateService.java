package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.dto.BookmarkResponseDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJpaRepository;
import com.kakao.linknamu.bookmarktag.service.BookmarkTagSearchService;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class BookmarkUpdateService {
	private final BookmarkJpaRepository bookmarkJpaRepository;
	private final BookmarkTagSearchService bookmarkTagSearchService;

	public BookmarkResponseDto.BookmarkUpdateResponseDto updateBookmark(
		BookmarkRequestDto.BookmarkUpdateRequestDto dto,
		Long userId,
		Long bookmarkId
	) {
		bookmarkJpaRepository.updateBookmark(bookmarkId, dto.bookmarkName(), dto.description());

		Bookmark bookmark = bookmarkJpaRepository.findByIdFetchJoinCategoryAndWorkspace(bookmarkId)
			.orElseThrow(() -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND));

		if (!bookmark.getCategory().getWorkspace().getUser().getUserId().equals(userId)) {
			throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
		}
		List<String> tags = bookmarkTagSearchService.searchTagNamesByBookmarkId(bookmarkId);

		return BookmarkResponseDto.BookmarkUpdateResponseDto.builder()
			.bookmarkId(bookmarkId)
			.title(bookmark.getBookmarkName())
			.description(bookmark.getBookmarkDescription())
			.url(bookmark.getBookmarkLink())
			.imageUrl(bookmark.getBookmarkThumbnail())
			.tags(tags)
			.build();
	}
}
